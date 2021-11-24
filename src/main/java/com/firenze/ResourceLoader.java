package com.firenze;

import com.firenze.annotation.Path;
import com.firenze.resolve.Resolver;
import com.firenze.resolve.Resource;
import com.firenze.resolve.Response;
import com.tw.FushengContainer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.reflections.Reflections;

import static com.firenze.http.HttpMethod.HTTP_METHOD_ANNOTATION_TYPE_COLLECTION;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.reflections.scanners.Scanners.SubTypes;

public class ResourceLoader {
    private FushengContainer fushengContainer;

    public List<Resolver> load(Class<?> source) {
        fushengContainer = FushengContainer.startup(source);
        Reflections reflections = new Reflections(source.getPackage().getName(), SubTypes.filterResultsBy(c -> true));
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
        List<Class<?>> rootResources = allClasses.stream()
                .filter(aClass -> Objects.nonNull(aClass.getDeclaredAnnotation(Path.class)))
                .collect(toList());
        return rootResources.stream()
                .map(resource -> loadResource(resource, ""))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private List<Resolver> loadResource(Class<?> root, String path) {
        String rootPath = ofNullable(root.getDeclaredAnnotation(Path.class)).map(Path::value).orElse(path);
        Object subject = fushengContainer.getComponent(root).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Duplicated resource " + root.getName()));
        List<Resolver> resolvers = new ArrayList<>();
        Arrays.stream(root.getMethods()).forEach(method -> {
            Path pathAnnotation = method.getDeclaredAnnotation(Path.class);
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            Class<?> methodClass = Arrays.stream(declaredAnnotations)
                    .map(Annotation::annotationType)
                    .filter(HTTP_METHOD_ANNOTATION_TYPE_COLLECTION::contains)
                    .findFirst().orElse(null);
            if (Objects.nonNull(methodClass)) {
                String currentPath = ofNullable(pathAnnotation).map(Path::value).orElse("");
                Response response = Response.of(path + rootPath + currentPath, methodClass.getSimpleName(), method, subject);
                resolvers.add(response);
            } else if (Objects.nonNull(pathAnnotation)) {
                String resourcePath = rootPath + pathAnnotation.value();
                List<Resolver> resolverList = loadResource(method.getReturnType(), resourcePath);
                Resource resource = Resource.of(resolverList);
                resolvers.add(resource);
            }
        });
        return resolvers;
    }
}
