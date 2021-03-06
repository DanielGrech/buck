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

package com.facebook.buck.cxx;

import com.facebook.buck.cli.BuckConfig;
import com.google.common.base.Optional;

import java.nio.file.Path;

public class InferBuckConfig {

  private static final String INFER_SECTION_PREFIX = "infer";

  private final BuckConfig delegate;

  public InferBuckConfig(BuckConfig delegate) {
    this.delegate = delegate;
  }

  public Optional<Path> getPath(String name) {
    return delegate.getPath(INFER_SECTION_PREFIX, name);
  }

  public Optional<String> getValue(String name) {
    return delegate.getValue(INFER_SECTION_PREFIX, name);
  }

}
