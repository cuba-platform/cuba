/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.sys.javacl.compiler;

import javax.tools.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A JavaFileManager which manages Java source and classes. This FileManager
 * delegates to the JavaFileManager and the ClassLoaderImpl provided in the
 * constructor. The sources are all in memory CharSequence instances and the
 * classes are all in memory byte arrays.
 *
 */
final class FileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {
   // the delegating class loader (passed to the constructor)
   private final ClassLoaderImpl classLoader;

   // Internal map of filename URIs to JavaFileObjects.
   private final Map<URI, JavaFileObject> fileObjects = new HashMap<>();

   /**
    * Construct a new FileManager which forwards to the <var>fileManager</var>
    * for source and to the <var>classLoader</var> for classes
    *
    * @param fileManager
    *           another FileManager that this instance delegates to for
    *           additional source.
    * @param classLoader
    *           a ClassLoader which contains dependent classes that the compiled
    *           classes will require when compiling them.
    */
   public FileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
      super(fileManager);
      this.classLoader = classLoader;
   }

   /**
    * @return the class loader which this file manager delegates to
    */
   public ClassLoader getClassLoader() {
      return classLoader;
   }

   /**
    * For a given file <var>location</var>, return a FileObject from which the
    * compiler can obtain source or byte code.
    *
    * @param location
    *           an abstract file location
    * @param packageName
    *           the package name for the file
    * @param relativeName
    *           the file's relative name
    * @return a FileObject from this or the delegated FileManager
    * @see javax.tools.ForwardingJavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location,
    *      String, String)
    */
   @Override
   public FileObject getFileForInput(Location location, String packageName,
         String relativeName) throws IOException {
      FileObject o = fileObjects.get(uri(location, packageName, relativeName));
      if (o != null)
         return o;
      return super.getFileForInput(location, packageName, relativeName);
   }

   /**
    * Store a file that may be retrieved later with
    * {@link #getFileForInput(javax.tools.JavaFileManager.Location, String, String)}
    *
    * @param location
    *           the file location
    * @param packageName
    *           the Java class' package name
    * @param relativeName
    *           the relative name
    * @param file
    *           the file object to store for later retrieval
    */
   public void putFileForInput(StandardLocation location, String packageName,
         String relativeName, JavaFileObject file) {
      fileObjects.put(uri(location, packageName, relativeName), file);
   }

   /**
    * Convert a location and class name to a URI
    */
   private URI uri(Location location, String packageName, String relativeName) {
      return CharSequenceCompiler.toURI(location.getName() + '/' + packageName + '/'
            + relativeName);
   }

   /**
    * Create a JavaFileImpl for an output class file and store it in the
    * classloader.
    *
    * @see javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location,
    *      String, javax.tools.JavaFileObject.Kind,
    *      javax.tools.FileObject)
    */
   @Override
   public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName,
         JavaFileObject.Kind kind, FileObject outputFile) throws IOException {
      JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
      classLoader.add(qualifiedName, file);
      return file;
   }

   @Override
   public ClassLoader getClassLoader(Location location) {
      return classLoader;
   }

   @Override
   public String inferBinaryName(Location loc, JavaFileObject file) {
      String result;
      // For our JavaFileImpl instances, return the file's name, else
      // simply run the default implementation
      if (file instanceof JavaFileObjectImpl)
         result = file.getName();
      else
         result = super.inferBinaryName(loc, file);
      return result;
   }

   @Override
   public Iterable<JavaFileObject> list(Location location, String packageName,
         Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
      Iterable<JavaFileObject> result = super.list(location, packageName, kinds,
            recurse);
      ArrayList<JavaFileObject> files = new ArrayList<>();
      if (location == StandardLocation.CLASS_PATH
            && kinds.contains(JavaFileObject.Kind.CLASS)) {
         for (JavaFileObject file : fileObjects.values()) {
            if (file.getKind() == JavaFileObject.Kind.CLASS && file.getName().startsWith(packageName))
               files.add(file);
         }
         files.addAll(classLoader.files());
      } else if (location == StandardLocation.SOURCE_PATH
            && kinds.contains(JavaFileObject.Kind.SOURCE)) {
         for (JavaFileObject file : fileObjects.values()) {
            if (file.getKind() == JavaFileObject.Kind.SOURCE && file.getName().startsWith(packageName))
               files.add(file);
         }
      }
      for (JavaFileObject file : result) {
         files.add(file);
      }
      return files;
   }
}