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

package com.facebook.buck.step.fs;

import com.facebook.buck.io.MorePaths;
import com.facebook.buck.io.ProjectFilesystem;
import com.facebook.buck.step.ExecutionContext;
import com.facebook.buck.step.Step;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.nio.file.Path;

public class SymlinkFileStep implements Step {

  private final ProjectFilesystem filesystem;
  private final Path existingFile;
  private final Path desiredLink;
  private final boolean useAbsolutePaths;

  public SymlinkFileStep(
      ProjectFilesystem filesystem,
      Path existingFile,
      Path desiredLink,
      boolean useAbsolutePaths) {
    this.filesystem = filesystem;
    this.existingFile = existingFile;
    this.desiredLink = desiredLink;
    this.useAbsolutePaths = useAbsolutePaths;
  }

  /**
   * Get the path to the existing file that should be linked.
   */
  private Path getExistingFilePath() {
    // This could be either an absolute or relative path.
    // TODO(edelron): Ideally all symbolic links should be relative, consider eliminating the
    // absolute option.
    return (useAbsolutePaths ? getAbsolutePath(existingFile, filesystem) :
        MorePaths.getRelativePath(existingFile, desiredLink.getParent()));
  }

  /**
   * Get the path to the desired link that should be created.
   */
  private Path getDesiredLinkPath() {
    return getAbsolutePath(desiredLink, filesystem);
  }

  private  Path getAbsolutePath(Path path, ProjectFilesystem filesystem) {
    return filesystem.getAbsolutifier().apply(path);
  }

  @Override
  public String getShortName() {
    return "symlink_file";
  }

  @Override
  public String getDescription (ExecutionContext context) {
    return Joiner.on(" ").join(
        "ln",
        "-f",
        "-s",
        getExistingFilePath(),
        getDesiredLinkPath());
  }

  @Override
  public int execute(ExecutionContext context) {
    Path existingFilePath = getExistingFilePath();
    Path desiredLinkPath = getDesiredLinkPath();
    try {
      filesystem.createSymLink(
          desiredLinkPath,
          existingFilePath,
          /* force */ true);
      return 0;
    } catch (IOException e) {
      e.printStackTrace(context.getStdErr());
      return 1;
    }
  }
}
