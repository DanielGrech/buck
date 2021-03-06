/*
 * Copyright 2015-present Facebook, Inc.
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
package com.facebook.buck.apple;

import static org.junit.Assert.assertThat;

import com.facebook.buck.testutil.integration.ProjectWorkspace;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import org.codehaus.plexus.util.StringUtils;
import org.hamcrest.Matchers;

import java.io.IOException;
import java.nio.file.Path;

public class AppleDsymTestUtil {
  private AppleDsymTestUtil() {}

  public static void checkDsymFileHasDebugSymbolForMain(
      ProjectWorkspace workspace,
      Path dwarfPath) throws IOException, InterruptedException {
    checkDsymFileHasDebugSymbolsForMainForConcreteArchitectures(workspace,
        dwarfPath,
        Optional.<ImmutableList<String>>absent());
  }

  public static void checkDsymFileHasDebugSymbolsForMainForConcreteArchitectures(
      ProjectWorkspace workspace,
      Path dwarfPath,
      Optional<ImmutableList<String>> architectures) throws IOException, InterruptedException {
    String dwarfdumpMainStdout =
        workspace.runCommand("dwarfdump", "-n", "main", dwarfPath.toString()).getStdout().or("");

    int expectedMatchCount = 1;
    if (architectures.isPresent()) {
      expectedMatchCount = architectures.get().size();
      for (String arch : architectures.get()) {
        assertThat(dwarfdumpMainStdout, Matchers.containsString(arch));
      }
    }

    assertThat(
        StringUtils.countMatches(
        dwarfdumpMainStdout, "AT_name"),
        Matchers.equalTo(expectedMatchCount));
    assertThat(
        StringUtils.countMatches(dwarfdumpMainStdout, "AT_decl_file"),
        Matchers.equalTo(expectedMatchCount));
    assertThat(
        StringUtils.countMatches(dwarfdumpMainStdout, "AT_decl_line"),
        Matchers.equalTo(expectedMatchCount));
  }
}
