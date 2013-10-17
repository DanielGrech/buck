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

package com.facebook.buck.util;

import com.google.common.hash.HashCode;

import java.nio.file.Path;

public class NullFileHashCache implements FileHashCache {

  @Override
  public boolean contains(Path path) {
    return false;
  }

  @Override
  public HashCode get(Path path) {
    return null;
  }

  @Override
  public void put(Path path, HashCode fileSha1) {
  }
}
