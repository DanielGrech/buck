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

package com.facebook.buck.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.facebook.buck.cli.BuckConfig;
import com.facebook.buck.cli.BuckConfigTestUtils;
import com.facebook.buck.cli.FakeBuckConfig;
import com.facebook.buck.testutil.integration.DebuggableTemporaryFolder;
import com.google.common.base.Joiner;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ParserConfigTest {

  @Rule
  public DebuggableTemporaryFolder temporaryFolder = new DebuggableTemporaryFolder();

  @Test
  public void testGetAllowEmptyGlobs() throws IOException {
    assertTrue(new ParserConfig(FakeBuckConfig.builder().build()).getAllowEmptyGlobs());
    Reader reader = new StringReader(
        Joiner.on('\n').join(
            "[build]",
            "allow_empty_globs = false"));
    ParserConfig config = new ParserConfig(
        BuckConfigTestUtils.createWithDefaultFilesystem(
            temporaryFolder,
            reader));
    assertFalse(config.getAllowEmptyGlobs());
  }

  @Test
  public void testGetGlobHandler() throws IOException {
    assertThat(
        new ParserConfig(FakeBuckConfig.builder().build()).getGlobHandler(),
        Matchers.equalTo(ParserConfig.GlobHandler.PYTHON));

    for (ParserConfig.GlobHandler handler : ParserConfig.GlobHandler.values()) {
      Reader reader = new StringReader(
          Joiner.on('\n').join(
              "[project]",
              "glob_handler = " + handler.toString()));
      ParserConfig config = new ParserConfig(
          BuckConfigTestUtils.createWithDefaultFilesystem(
              temporaryFolder,
              reader));
      assertThat(config.getGlobHandler(), Matchers.equalTo(handler));
    }
  }

  @Test
  public void shouldReturnThreadCountIfParallelParsingIsEnabled() {
    BuckConfig config = FakeBuckConfig.builder()
        .setSections(
            "[project]",
            "parsing_threads = 5",
            "parallel_parsing = true")
        .build();

    ParserConfig parserConfig = new ParserConfig(config);

    assertTrue(parserConfig.getEnableParallelParsing());
    assertEquals(5, parserConfig.getNumParsingThreads());
  }

  @Test
  public void shouldReturnOneThreadCountIfParallelParsingIsNotEnabled() {
    BuckConfig config = FakeBuckConfig.builder()
        .setSections(
            "[project]",
            "parsing_threads = 5",
            "parallel_parsing = false")
        .build();

    ParserConfig parserConfig = new ParserConfig(config);

    assertFalse(parserConfig.getEnableParallelParsing());
    assertEquals(1, parserConfig.getNumParsingThreads());
  }
}
