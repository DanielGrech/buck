/*
 * Copyright 2012-present Facebook, Inc.
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

package com.facebook.buck.rules;

import com.facebook.buck.io.ProjectFilesystem;
import com.facebook.buck.model.BuildFileTree;
import com.facebook.buck.model.BuildTarget;
import com.facebook.buck.model.Flavor;

public final class BuildRuleFactoryParams {

  private final ProjectFilesystem filesystem;
  public final BuildTarget target;
  private final BuildFileTree buildFileTree;
  private final boolean enforceBuckPackageBoundary;

  public BuildRuleFactoryParams(
      ProjectFilesystem filesystem,
      BuildTarget target,
      BuildFileTree buildFileTree,
      boolean enforceBuckPackageBoundary) {
    this.filesystem = filesystem;
    this.target = target;
    this.buildFileTree = buildFileTree;
    this.enforceBuckPackageBoundary = enforceBuckPackageBoundary;
  }

  public ProjectFilesystem getProjectFilesystem() {
    return filesystem;
  }

  public BuildFileTree getBuildFileTree() {
    return buildFileTree;
  }

  public boolean enforceBuckPackageBoundary() {
    return enforceBuckPackageBoundary;
  }

  public boolean isSameTarget(BuildRuleFactoryParams other) {
    return target.equals(other.target) &&
        filesystem.equals(other.filesystem);
  }

  public BuildRuleFactoryParams withFlavors(Iterable<Flavor> flavors) {
    return new BuildRuleFactoryParams(
        filesystem,
        target.withFlavors(flavors),
        buildFileTree,
        enforceBuckPackageBoundary);
  }
}
