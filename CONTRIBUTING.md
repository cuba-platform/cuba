# Contributing to CUBA Platform

These instructions are for contributing code to the core framework.

## Reporting bugs

If you want to discuss your problem or ask something please use our forum: https://www.cuba-platform.com/discuss. Search through existing issues before adding a new one - there is a good chance someone has already faced the same problem. Comment on existing issues to let us know that it is relevant to you or to add more details.

## Submitting patches

All our projects accept contributions as GitHub pull requests. The first time you create a pull request, you will be asked to electronically sign a contribution agreement.

https://yangsu.github.io/pull-request-tutorial/ has instructions on how to create a pull request.

Remember to check the "Allow edits from maintainers" so we can rebase the PR or make small changes if necessary.

Usually, we create an issue for the PR in our internal bug tracker (YouTrack) and add the issue number to the PR title.

## 5 Steps to contribute a patch to CUBA Platform

1. Create a pull request
1. Sign a CLA
1. Respond to review comments
1. Fix code and style issues
1. Wait for merge and release

## Project setup
CUBA Platform consist of two main projects:
- cuba-gradle-plugin https://github.com/cuba-platform/cuba-gradle-plugin 
- cuba https://github.com/cuba-platform/cuba  

### Building from Source
Let's assume that you have cloned CUBA Gradle Plugin and CUBA into the following directories:
```
work/
    cuba/
    cuba-gradle-plugin/
```

All CUBA build process details and Gradle tasks are implemented in cuba-gradle-plugin.

Open terminal in the work directory and run the following command to build and install the plugin into your local Maven repository (~/.m2):
```
> cd cuba-gradle-plugin
> gradlew install
```

It will build and install -SNAPSHOT version of cuba-gradle-plugin required to build the platform.

After that, go to the CUBA directory and build and install it with the same command:
```
> cd ../cuba
> gradlew install
```

If there are any errors during the compilation please check our public build status at https://travis-ci.org/cuba-platform Do not hesitate to report us any problems with build!

### Importing to IDE

We use IntelliJ Idea IDE for development.

Generate IntelliJ Idea project files
```
> gradlew assemble idea 
```

Open the .ipr file and start working on the project

### Starting CUBA as an application

CUBA itself can be running as an application with in-memory HSQL database. It is really handy if you want to check changes in the standard UI functionality, so you don’t need to create additional sample projects.

Start in-memory DB first:
```
> gradlew startDb createDb
```

Extract local tomcat instance (tomcat will be extracted to work directory on the same level as cuba and cuba-gradle-plugin directories):
```
> gradlew setupTomcat
```

Deploy and start cuba:
```
> gradlew deploy start
```

CUBA web UI will be available at http://localhost:8080/cuba/ 

Use the following command to deploy and restart application after small changes:
```
> gradlew restart
```

### Running tests
CUBA includes standard unit tests and integration tests that require in-memory database and Spring context. See this documentation section: https://doc.cuba-platform.com/manual-latest/testing.html 

To start all the tests use the following command:
```
> gradlew startDb createTestDb test
```

Also, you can easily start particular test from IntelliJ Idea, but remember to start HSQL DB and create test database before running middleware integration tests.

### Static code analysis

Start the following command to run static code analysis:
```
> gradlew javadoc findbugsMain
```

## Development process guidelines

### Code style

1. Source code files (Java, XML and Groovy) must have copyright notice with Apache 2.0 license. It will be generated automatically if you use IntelliJ Idea IDE.
1. Use default IntelliJ Idea code formatting options. You can reformat your code (reformat changed code only!) using Ctrl+Alt+L shortcut.
1. Maximum line length - 120 symbols.
1. Recommended method length - up to 50 lines.
1. All public interfaces, classes and their public methods must have JavaDoc statement (excluding entities and simple getters/setters).
1. All overridden methods should have @Override annotation.
1. SQL reserved words - lowercase.
1. SQL identifiers (table names, column names, etc) - UPPERCASE_WITH_UNDERSCORES.
1. If you change data model - you have to provide SQL create / update scripts for all supported databases: HSQL, PostgreSQL, MySQL, MS SQL, Oracle.
1. SQL Update scripts must be named as <yymmdd>-yourScriptName.sql: 170719-addedAuditTable.sql

### Pull-requests

1. All feature branches must be named as “feature/some-feature-name”. Do not name your branch as YouTrack issue number, branch name should describe the purpose of the branch.
1. If you solve an existing problem that is described in one of the issues in our internal bug tracker please add issue number at the start of your commit message. If a problem is not stated in our bug tracker - we will create an issue and add issue number to commit message while merging a pull-request.
1. Solve only one problem per patch.
1. Describe your changes and user-visible impact: what did you change, why did you change it, how did you change it?
1. Include a test to prove your patch works. Unit tests are preferred. 
1. Style-check your changes: it’s okay to have a separate commit to fix style issues.
1. Build project and run tests before submitting a patch.
1. Create a pull request; it will then be reviewed by the platform team. Remember to check the "Allow edits from maintainers" so we can rebase the PR or make small changes if necessary.
1. After you have submitted your change, be patient and wait. Reviewers are busy people and may not get to your patch right away. Ideally, we try to get a response within one business day.
1. Respond to review comments: review comments are meant to improve the quality of the code by pointing out defects or readability issues.
1. Most PRs take a few iterations of review before they are merged.

__We are looking forward to getting your contributions!__
