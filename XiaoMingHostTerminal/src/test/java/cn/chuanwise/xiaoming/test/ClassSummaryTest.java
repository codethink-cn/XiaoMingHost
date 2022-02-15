package cn.chuanwise.xiaoming.test;

import cn.chuanwise.util.ObjectUtil;
import cn.chuanwise.util.StringUtil;
import cn.chuanwise.util.CollectionUtil;
import cn.chuanwise.xiaoming.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class ClassSummaryTest {
    public static void main(String[] args) {
        final Class<?> clazz = Plugin.class;
        final Pattern namePattern = Pattern.compile("\\w+");

        final Map<String, Set<Method>> overloadedMethods = new HashMap<>();
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            final String methodName = method.getName();

            if (!namePattern.matcher(methodName).matches()) {
                continue;
            }

            Set<Method> sameNameMethods = overloadedMethods.get(methodName);
            if (CollectionUtil.isEmpty(sameNameMethods)) {
                sameNameMethods = new HashSet<>();
                overloadedMethods.put(methodName, sameNameMethods);
            }

            sameNameMethods.add(method);
        }
        /*
|返回类型|原型|异常类型|说明|
|---|---|---|---|
         */

        boolean hasException = Objects.nonNull(CollectionUtil.first(overloadedMethods.values(), set -> Objects.nonNull(CollectionUtil.first(set, method -> {
            return method.getExceptionTypes().length != 0;
        }))));

        if (hasException) {
            System.out.println("|返回类型|原型|异常类型|说明|\n" +
                    "|---|---|---|---|");
        } else {
            System.out.println("|返回类型|原型|说明|\n" +
                    "|---|---|---|");
        }

        for (Map.Entry<String, Set<Method>> entry : overloadedMethods.entrySet()) {
            final String methodName = entry.getKey();
            final Set<Method> sameNameMethods = entry.getValue();

            // System.out.println(methodName);
            if (sameNameMethods.size() > 1) {
                System.out.println("### " + methodName + "\n" +
                        "该方法有 `" + sameNameMethods.size() + "` 个重载版本：\n" +
                        "|返回类型|原型|异常类型|说明|\n" +
                        "|---|---|---|---|");
            }
            for (Method method : sameNameMethods) {
                final String parameterNames = CollectionUtil.toString(Arrays.asList(method.getParameters()), parameter -> {
                    final String simpleName = parameter.getType().getSimpleName();
                    return simpleName + " " + StringUtil.humpToHump(simpleName, false);
                }, ", ");
                final String exceptionTypes = CollectionUtil.toString(Arrays.asList(method.getExceptionTypes()), type -> {
                    return "`" + type.getSimpleName() + "`";
                }, ", ");

                final String beforeException = "|`" + method.getReturnType().getSimpleName() + "`|`" + methodName +
                        "(" + ObjectUtil.firstNonNull(parameterNames, "") + ")`|";
                if (hasException) {
                    System.out.println(beforeException + ObjectUtil.firstNonNull(exceptionTypes, "") + "||");
                } else {
                    System.out.println(beforeException + "|");
                }
            }
        }
    }
}
