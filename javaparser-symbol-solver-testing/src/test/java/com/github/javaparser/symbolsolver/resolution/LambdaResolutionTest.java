/*
 * Copyright (C) 2015-2016 Federico Tomassetti
 * Copyright (C) 2017-2024 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package com.github.javaparser.symbolsolver.resolution;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.resolution.Navigator;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class LambdaResolutionTest extends AbstractResolutionTest {

    @Test
    void lambdaMapParameter() {
        CompilationUnit cu = parseSample("Lambda");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "lambdaMap");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        MethodCallExpr methodCallExpr =
                (MethodCallExpr) returnStmt.getExpression().get();
        Expression expression = methodCallExpr.getArguments().get(0);

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type = javaParserFacade.getType(expression);
        assertEquals(
                "java.util.function.Function<? super java.lang.String, ? extends java.lang.String>", type.describe());
    }

    @Test
    void personsStream() {
        CompilationUnit cu = parseSample("Lambda");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "lambdaMap");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        Expression expression = returnStmt.getExpression().get();
        expression = Navigator.findMethodCall(expression, "stream").get();

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type = javaParserFacade.getType(expression);
        assertEquals("java.util.stream.Stream<java.lang.String>", type.describe());
    }

    @Test
    void lambdaMap() {
        CompilationUnit cu = parseSample("Lambda");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration m1 = Navigator.demandMethod(clazz, "lambdaMap");
        MethodDeclaration m2 = Navigator.demandMethod(clazz, "lambdaMap2");
        ReturnStmt returnStmt1 = Navigator.demandReturnStmt(m1);
        ReturnStmt returnStmt2 = Navigator.demandReturnStmt(m2);
        Expression e1 = returnStmt1.getExpression().get();
        Expression e2 = returnStmt2.getExpression().get();

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type1 = javaParserFacade.getType(e1);
        ResolvedType type2 = javaParserFacade.getType(e2);
        assertEquals("java.util.stream.Stream<java.lang.String>", type1.describe());
        assertEquals("java.util.stream.Stream<java.util.stream.IntStream>", type2.describe());
    }

    @Test
    void lambdaReduce() {
        CompilationUnit cu = parseSample("Lambda");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "reduce");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        Expression expr = returnStmt.getExpression().get();

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type1 = javaParserFacade.getType(expr);
        assertEquals("java.util.Optional<java.lang.Integer>", type1.describe());
    }

    @Test
    void lambdaPrint() {
        CompilationUnit cu = parseSample("Lambda");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "print");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        Expression expr = returnStmt.getExpression().get();

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type1 = javaParserFacade.getType(expr);
        assertEquals("void", type1.describe());
    }

    @Test
    void lambdaBifunc() {
        CompilationUnit cu = parseSample("Lambda");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "bifunc");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        Expression expr = returnStmt.getExpression().get();

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type1 = javaParserFacade.getType(expr);
        assertEquals("double", type1.describe());
    }

    @Test
    void lambdaCollectParam() {
        CompilationUnit cu = parseSample("LambdaCollect");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "lambdaMap");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        MethodCallExpr methodCallExpr =
                (MethodCallExpr) returnStmt.getExpression().get();
        // Collectors.toList()
        Expression expression = methodCallExpr.getArguments().get(0);

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type = javaParserFacade.getType(expression);
        assertEquals("java.util.stream.Collector<T, ?, java.util.List<T>>", type.describe());
    }

    @Test
    void lambdaCollect() {
        CompilationUnit cu = parseSample("LambdaCollect");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "lambdaMap");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        Expression expression = returnStmt.getExpression().get();

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type = javaParserFacade.getType(expression);
        assertEquals("java.util.List<java.lang.String>", type.describe());
    }

    @Test
    void lambdaBlockExplicitReturn() {
        CompilationUnit cu = parseSample("LambdaMulti");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "lambdaSingleReturn");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        Expression expression = returnStmt.getExpression().get();

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type = javaParserFacade.getType(expression);
        assertEquals("java.lang.String", type.describe());
    }

    @Test
    void lambdaBlockMultiLineReturn() {
        CompilationUnit cu = parseSample("LambdaMulti");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "multiLineReturn");
        ReturnStmt returnStmt = Navigator.demandReturnStmt(method);
        Expression expression = returnStmt.getExpression().get();

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type = javaParserFacade.getType(expression);
        assertEquals("java.lang.String", type.describe());
    }

    @Test
    void typeOfVoidLambda() {
        CompilationUnit cu = parseSample("LambdaVoid");
        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Agenda");
        MethodDeclaration method = Navigator.demandMethod(clazz, "lambdaEmpty");
        MethodCallExpr methodCallExpr =
                Navigator.findMethodCall(method, "forEach").get();
        LambdaExpr lambdaExpr = (LambdaExpr) methodCallExpr.getArguments().get(0);

        JavaParserFacade javaParserFacade = JavaParserFacade.get(new ReflectionTypeSolver());
        ResolvedType type = javaParserFacade.getType(lambdaExpr);
        assertEquals("java.util.function.Consumer<? super java.lang.String>", type.describe());
    }

    @Test
    void lambdaAsVararg() {
        String source = "import java.util.function.Consumer;\n" + "class Test {\n"
                + "    void acceptConsumers(Consumer<String>... consumers) {}\n"
                + "    void test(Consumer<String> first) {\n"
                + "        acceptConsumers(first, s -> System.out.println(s));\n"
                + "    }\n"
                + "}";

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
        final CompilationUnit cu = StaticJavaParser.parse(source);
        final LambdaExpr lambda = cu.findFirst(LambdaExpr.class).get();
        assertEquals(
                "java.util.function.Consumer<java.lang.String>",
                lambda.calculateResolvedType().describe());
    }

    @Test
    void lambdaOverloadsWithDifferentParameterCounts1() {
        String source = "import java.util.function.Consumer;\n" + "class Test {\n"
                + "    void foo(Consumer<String> consumer) {}\n"
                + "    void foo(Runnable r) {}\n"
                + "    void test() {\n"
                + "        foo(input -> {});\n"
                + "    }\n"
                + "}";

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
        final CompilationUnit cu = StaticJavaParser.parse(source);
        final MethodCallExpr call = cu.findFirst(MethodCallExpr.class).get();
        assertEquals(
                "Test.foo(java.util.function.Consumer<java.lang.String>)",
                call.resolve().getQualifiedSignature());
        assertEquals("void", call.calculateResolvedType().describe());
    }

    @Test
    void lambdaOverloadsWithDifferentParameterCounts2() {
        String source = "import java.util.function.Consumer;\n" + "class Test {\n"
                + "    void foo(Consumer<java.lang.String> consumer) {}\n"
                + "    void foo(Runnable r) {}\n"
                + "    void test() {\n"
                + "        foo(() -> {});\n"
                + "    }\n"
                + "}";

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
        final CompilationUnit cu = StaticJavaParser.parse(source);
        final MethodCallExpr call = cu.findFirst(MethodCallExpr.class).get();
        assertEquals("Test.foo(java.lang.Runnable)", call.resolve().getQualifiedSignature());
        assertEquals("void", call.calculateResolvedType().describe());
    }

    @Test
    void lambdaOverloadsWithDifferentReturnTypes1() {
        String source = "import java.util.function.Consumer;\n" + "import java.util.function.Function;\n"
                + "class Test {\n"
                + "    void foo(Consumer<String> consumer) {}\n"
                + "    void foo(Function<Integer, String> func) {}\n"
                + "    void test() {\n"
                + "        foo(input -> {});\n"
                + "    }\n"
                + "}";

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
        final CompilationUnit cu = StaticJavaParser.parse(source);
        final MethodCallExpr call = cu.findFirst(MethodCallExpr.class).get();
        assertEquals(
                "Test.foo(java.util.function.Consumer<java.lang.String>)",
                call.resolve().getQualifiedSignature());
        assertEquals("void", call.calculateResolvedType().describe());
    }

    @Test
    void lambdaOverloadsWithDifferentReturnTypes2() {
        String source = "import java.util.function.Consumer;\n" + "import java.util.function.Function;\n"
                + "class Test {\n"
                + "    void foo(Consumer<String> consumer) {}\n"
                + "    void foo(Function<Integer, String> func) {}\n"
                + "    void test() {\n"
                + "        foo(input -> { return \"\"; });\n"
                + "    }\n"
                + "}";

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
        final CompilationUnit cu = StaticJavaParser.parse(source);
        final MethodCallExpr call = cu.findFirst(MethodCallExpr.class).get();
        assertEquals(
                "Test.foo(java.util.function.Function<java.lang.Integer, java.lang.String>)",
                call.resolve().getQualifiedSignature());
        assertEquals("void", call.calculateResolvedType().describe());
    }

    @Test
    void lambdaUsedAsPolymorphicArgument() {
        String source = "import java.util.function.Consumer;\n" + "import java.util.HashMap;"
                + "class Test {\n"
                + "    void test() {\n"
                + "        HashMap<String, Consumer> map = new HashMap<>();"
                + "        map.put(\"\", input -> {});\n"
                + "    }\n"
                + "}";

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
        final CompilationUnit cu = StaticJavaParser.parse(source);
        final MethodCallExpr call = cu.findFirst(MethodCallExpr.class).get();
        assertDoesNotThrow(() -> call.resolve().getQualifiedSignature());
        assertDoesNotThrow(() -> call.calculateResolvedType().describe());
        assertEquals("java.util.HashMap.put(K, V)", call.resolve().getQualifiedSignature());
    }

    @Test
    void lambdaUsedAsOverloadedArrayAlternativeArgument() {
        String source = "import java.util.function.Consumer;\n" + "import java.util.function.Function;\n"
                + "class Foo<S extends Consumer, T> {\n"
                + "    void foo(Object[] ts) {}\n"
                + "    void foo(T t) {}\n"
                + "}\n"
                + "class Test {\n"
                + "    void test() {\n"
                + "        Foo<Consumer<Integer>, Function<Integer, Integer>> foo = new Foo<>();\n"
                + "        foo.foo(value -> { return 2; });\n"
                + "    }\n"
                + "}";

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
        final CompilationUnit cu = StaticJavaParser.parse(source);
        final MethodCallExpr call = cu.findFirst(MethodCallExpr.class).get();
        assertDoesNotThrow(() -> call.resolve().getQualifiedSignature());
        assertDoesNotThrow(() -> call.calculateResolvedType().describe());
        assertEquals("Foo.foo(T)", call.resolve().getQualifiedSignature());
    }

    @Disabled("Disambiguation for lambdas used as polymorphic is not supported yet.")
    @Test
    void lambdaUsedAsOverloadedPolymorphicArgument1() {

        String source = "import java.util.function.Consumer;\n" + "import java.util.function.Function;\n"
                + "class Foo<S extends Consumer, T> {\n"
                + "    void foo(T t) {}\n"
                + "    void foo(S s) {}\n"
                + "}\n"
                + "class Test {\n"
                + "    void test() {\n"
                + "        Foo<Consumer<Integer>, Function<Integer, Integer>> foo = new Foo<>();\n"
                + "        foo.foo(value -> { return 2; });\n"
                + "    }\n"
                + "}";

        StaticJavaParser.getParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
        final CompilationUnit cu = StaticJavaParser.parse(source);
        final MethodCallExpr call = cu.findFirst(MethodCallExpr.class).get();
        assertDoesNotThrow(() -> call.resolve().getQualifiedSignature());
        assertDoesNotThrow(() -> call.calculateResolvedType().describe());
        assertEquals("Foo.foo(java.util.function.Function)", call.resolve().getQualifiedSignature());
    }
}
