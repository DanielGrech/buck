/*
 * Copyright 2013-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.step;

import com.facebook.buck.event.BuckEventBusFactory;
import com.facebook.buck.jvm.java.FakeJavaPackageFinder;
import com.facebook.buck.testutil.TestConsole;
import com.facebook.buck.util.ClassLoaderCache;
import com.facebook.buck.util.environment.Platform;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecutionContext {

  private TestExecutionContext() {
    // Utility class.
  }

  // For test code, use a global class loader cache to avoid having to call ExecutionContext.close()
  // in each test case.
  private static ClassLoaderCache testClassLoaderCache = new ClassLoaderCache();

  public static ExecutionContext.Builder newBuilder() {
    Map<ExecutionContext.ExecutorPool, ExecutorService> executors =
        new HashMap<ExecutionContext.ExecutorPool, ExecutorService>();
    executors.put(ExecutionContext.ExecutorPool.CPU, Executors.newCachedThreadPool());
    return ExecutionContext.builder()
        .setConsole(new TestConsole())
        .setEventBus(BuckEventBusFactory.newInstance())
        .setPlatform(Platform.detect())
        .setEnvironment(ImmutableMap.copyOf(System.getenv()))
        .setJavaPackageFinder(new FakeJavaPackageFinder())
        .setObjectMapper(new ObjectMapper())
        .setClassLoaderCache(testClassLoaderCache)
        .setExecutors(executors);
  }

  public static ExecutionContext newInstance() {
    return newBuilder().build();
  }
}
