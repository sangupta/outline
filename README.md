# Outline

`Outline` is a Java-annotation based command line argument parser loosely based on 
https://github.com/airlift/airline.

The need for `Outline` arose as the development of `airline` is mostly stalled - no
new commits, bug-fixes, or pull-request merges have been made. I tried to use to fix the
same in a fork of mine - but then its very hard dependency on `Google Guava` made me
realize that we could simplify the framework. `airline` is dependent a lot on **immutables**
and thus, copies a lot of data between data-structures which bloats the code to understand.

Thus, began my journey for `Outline` with an aim to simplify the framework and yet be
more-powerful than `airline`.

The codebase is now sane and it can be used to parse almost all Git-like command-structures.
Some examples are listed below.

## TODO

Following items are still need to be implemented to attain feature parity with `airline`:

* Generate help guides
* Single command interface - would be good if we can support it without any special coding conventions
* Allow collecting all available commands using reflection
* support for system properties
* support for reading configuration from file
* resolve conflicting names of commands across groups

## Features

Features that are not available in `airline`:

* Support for splitting end arguments based on index into individual attributes
* Ability to define command groups using annotation

Common feature set:



## Usage Examples

## Downloads

For now you may use **jitpack.io** to download **snapshots** of the framework:

Add the following `repository` to Maven,

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Then add the dependency as,

```xml
<dependency>
    <groupId>com.github.sangupta</groupId>
    <artifactId>outline</artifactId>
    <version>-SNAPSHOT</version>
</dependency>
```

## Versioning

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`Outline` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

## License
    
```
outline - command line argument parser
Copyright (c) 2015-2016, Sandeep Gupta

http://sangupta.com/projects/outline

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```