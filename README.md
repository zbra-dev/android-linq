# Android LINQ
Manipulate collections easily using C# LINQ style queries and, preferably, Java 8 closures.

# Description

Android LINQ is a small subset of collection manipulation utilities inspirded by Microsoft C# LINQ library and targeted at Android developers looking to use new Java 8 Stream() API. 

By using [Retrolambda for Android](https://github.com/evant/gradle-retrolambda) developers can leverage the power of closures and other new Java 8 features. Unfortunately, it doesn't allow the usage of the Stream API which is arguably its most awesome feature. However, by using it in conjunction with Android LINQ, its possible to perform powerful collection manipulation in just a few lines of code. 

All in all, if you are not huge fan of Java 8 closures and stuff, you can use Android LINQ stand alone with default Anonymous Class implementations, but this will make things far less attractive to the eyes I must admit. Still feasible...

Android LINQ has little to no impact on performance because it does not make use of reflection or proxies. As its C# counterpart it based on the [monads](http://en.wikipedia.org/wiki/Monad_(functional_programming)) concept, which is a fancy word to describe a sort of [Decorator](http://en.wikipedia.org/wiki/Decorator_pattern) pattern implementation, and many sorting and ordering are just making calls to the default Java API.

Anyway, you need not to worry. Just add this to your Gradle/Maven and suffer with manual collection iteration no more!

# Usage

### Maven

Coming soon!

### Gradle

Coming soon!

# Examples

Coming soon!

# Pull Requests Are Welcome!

Please, send feedback and pull requests a plenty! 
If you find a bug, a missing feature or have an improvement suggestion, don't be afraid to file an issue and we will do our best to attend it.
