package io.vertx.test.codegen;

import io.vertx.codegen.ClassKind;
import io.vertx.codegen.TypeInfo;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.test.codegen.testapi.GenericInterface;
import io.vertx.test.codegen.testapi.InterfaceWithParameterizedDeclaredSupertype;
import io.vertx.test.codegen.testapi.InterfaceWithParameterizedVariableSupertype;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TypeInfoTest {

  @Test
  public void testVoid() throws Exception {
    TypeInfo.Void info = (TypeInfo.Void) TypeInfo.create(void.class);
    assertEquals("void", info.getName());
    assertEquals("void", info.getSimpleName());
  }

  @Test
  public void testPrimitive() throws Exception {
    TypeInfo.Primitive info = (TypeInfo.Primitive) TypeInfo.create(int.class);
    assertEquals("int", info.getName());
    assertEquals("int", info.getSimpleName());
  }

  @Test
  public void testParameterizedWithClass() {
    TypeInfo.Parameterized info = (TypeInfo.Parameterized) TypeInfo.create(InterfaceWithParameterizedDeclaredSupertype.class.getGenericInterfaces()[0]);
    assertEquals("io.vertx.test.codegen.testapi.GenericInterface<java.lang.String>", info.getName());
    assertEquals("GenericInterface<String>", info.getSimpleName());
  }

  @Test
  public void testParameterizedWithTypeVariable() {
    TypeInfo.Parameterized info = (TypeInfo.Parameterized) TypeInfo.create(InterfaceWithParameterizedVariableSupertype.class.getGenericInterfaces()[0]);
    assertEquals("io.vertx.test.codegen.testapi.GenericInterface<T>", info.getName());
    assertEquals("GenericInterface<T>", info.getSimpleName());
  }

  @Test
  public void testTypeVariable() throws Exception {
    Method m = GenericInterface.class.getMethods()[0];
    TypeInfo.Variable info = (TypeInfo.Variable) TypeInfo.create(m.getGenericReturnType());
    assertEquals("T", info.getName());
    assertEquals("T", info.getSimpleName());
  }

  @Test
  public void testClass() {
    TypeInfo.Class info = (TypeInfo.Class) TypeInfo.create(String.class);
    assertEquals("java.lang.String", info.getName());
    assertEquals("String", info.getSimpleName());
  }

  @Test
  public void testInterface() {
    TypeInfo.Class info = (TypeInfo.Class) TypeInfo.create(Runnable.class);
    assertEquals("java.lang.Runnable", info.getName());
    assertEquals("Runnable", info.getSimpleName());
  }

  // TypeKind testing

  @Test
  public void testComposeKinds() {
    abstract class Container implements AsyncResult<List<String>>  {}
    TypeInfo.Parameterized info = (TypeInfo.Parameterized) TypeInfo.create(Container.class.getGenericInterfaces()[0]);
    assertEquals(ClassKind.ASYNC_RESULT, info.getKind());
    TypeInfo.Parameterized a = (TypeInfo.Parameterized) info.getArgs().get(0);
    assertEquals(ClassKind.LIST, a.getKind());
    TypeInfo.Class b = (TypeInfo.Class) a.getArgs().get(0);
    assertEquals(ClassKind.STRING, b.getKind());
  }

  @Test
  public void testPrimitiveKind() {

    @VertxGen class ApiObject {}
    @DataObject
    class DataObjectObject {}
    class Other {}

    assertEquals(ClassKind.OTHER, TypeInfo.create(Other.class).getKind());
    assertEquals(ClassKind.DATA_OBJECT, TypeInfo.create(DataObjectObject.class).getKind());
    assertEquals(ClassKind.API, TypeInfo.create(ApiObject.class).getKind());
    assertEquals(ClassKind.HANDLER, TypeInfo.create(Handler.class).getKind());
    assertEquals(ClassKind.ASYNC_RESULT, TypeInfo.create(AsyncResult.class).getKind());
    assertEquals(ClassKind.VOID, TypeInfo.create(Void.class).getKind());
    assertEquals(ClassKind.JSON_ARRAY, TypeInfo.create(JsonArray.class).getKind());
    assertEquals(ClassKind.JSON_OBJECT, TypeInfo.create(JsonObject.class).getKind());
    assertEquals(ClassKind.OBJECT, TypeInfo.create(Object.class).getKind());
    assertEquals(ClassKind.STRING, TypeInfo.create(String.class).getKind());
    assertEquals(ClassKind.LIST, TypeInfo.create(List.class).getKind());
    assertEquals(ClassKind.SET, TypeInfo.create(Set.class).getKind());
    assertEquals(ClassKind.THROWABLE, TypeInfo.create(Throwable.class).getKind());
    assertEquals(ClassKind.PRIMITIVE, TypeInfo.create(boolean.class).getKind());
    assertEquals(ClassKind.PRIMITIVE, TypeInfo.create(int.class).getKind());
    assertEquals(ClassKind.PRIMITIVE, TypeInfo.create(long.class).getKind());
    assertEquals(ClassKind.PRIMITIVE, TypeInfo.create(double.class).getKind());
    assertEquals(ClassKind.PRIMITIVE, TypeInfo.create(float.class).getKind());
    assertEquals(ClassKind.PRIMITIVE, TypeInfo.create(byte.class).getKind());
    assertEquals(ClassKind.PRIMITIVE, TypeInfo.create(short.class).getKind());
    assertEquals(ClassKind.PRIMITIVE, TypeInfo.create(char.class).getKind());
    assertEquals(ClassKind.BOXED_PRIMITIVE, TypeInfo.create(Boolean.class).getKind());
    assertEquals(ClassKind.BOXED_PRIMITIVE, TypeInfo.create(Integer.class).getKind());
    assertEquals(ClassKind.BOXED_PRIMITIVE, TypeInfo.create(Long.class).getKind());
    assertEquals(ClassKind.BOXED_PRIMITIVE, TypeInfo.create(Double.class).getKind());
    assertEquals(ClassKind.BOXED_PRIMITIVE, TypeInfo.create(Float.class).getKind());
    assertEquals(ClassKind.BOXED_PRIMITIVE, TypeInfo.create(Byte.class).getKind());
    assertEquals(ClassKind.BOXED_PRIMITIVE, TypeInfo.create(Short.class).getKind());
    assertEquals(ClassKind.BOXED_PRIMITIVE, TypeInfo.create(Character.class).getKind());
  }

  @Test
  public void testBoxedPrimitiveKind() {
  }

  @Test
  public void testGetErased() {
    abstract class Container<M> implements AsyncResult<List<M>>  {}
    abstract class Expected implements AsyncResult<List<Object>>  {}
    TypeInfo.Parameterized info = (TypeInfo.Parameterized) TypeInfo.create(Container.class.getGenericInterfaces()[0]);
    TypeInfo.Parameterized expected = (TypeInfo.Parameterized) TypeInfo.create(Expected.class.getGenericInterfaces()[0]);
    assertEquals(expected, info.getErased());
  }
}
