# Android LINQ

Manipulate collections easily using C# LINQ style queries and Java 8 closures.

## Description

Android LINQ is a small subset of collection manipulation utilities inspired by Microsoft C# LINQ library and targeted at Android developers looking to use new Java 8 Stream() API. 

By using [Retrolambda for Android](https://github.com/evant/gradle-retrolambda), developers can leverage the power of closures and other new Java 8 features. Unfortunately, it doesn't allow the usage of the Stream API which is arguably its most awesome feature. However, by using it in conjunction with Android LINQ, its possible to perform powerful collection manipulation in just a few lines of code. 

Android LINQ has little to no impact on performance because it does not make use of reflection or proxies. As its C# counterpart it's based on the [monads](http://en.wikipedia.org/wiki/Monad_(functional_programming)) concept, which is a fancy word to describe a sort of [Decorator](http://en.wikipedia.org/wiki/Decorator_pattern) pattern implementation, and many sorting and ordering are just making calls to the default Java API.

Anyway, you need not to worry. Just add this to your Gradle/Maven and suffer with manual collection iteration no more!

## Usage

### Latest Version 

[ ![Download](https://api.bintray.com/packages/brunovinicius/maven/android-linq/images/download.svg) ](https://bintray.com/brunovinicius/maven/android-linq/_latestVersion)

### Android

To use Android LINQ, first, go and setup [Retrolambda for Android](https://github.com/evant/gradle-retrolambda) so we can use those fancy closures from Java 8 (don't worry, its just some extra lines on your build.gradle file). 

Now, just add this line to your project build.gradle (files are hosted in Bintray jCenter, so don't forget to add it to the repositories list too).

#### Gradle

```
...
repositories {
    jcenter()
}
...
```
```
compile 'br.com.zbra:android-linq:1.0.1'
```

### Standard Java 8

Android LINQ uses standard Java and therefore can also be used outside Android.

#### Maven

```
<repositories>
    ...
    <repository>
      <id>jcenter</id>
      <url>http://jcenter.bintray.com </url>
      ...
    </repository>
    ...
</repositories>
```
```
<dependency>
  <groupId>br.com.zbra</groupId>
  <artifactId>android-linq</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Examples

#### Get names from contacts
```
List<String> contactNames = 
      stream(contacts)
          .select(c -> c.getName())
          .toList();
````
#### Get contacts who are 27 years or older
```
List<Contact> contacts = 
      stream(contacts)
          .where(c -> c.getAge() >= 27)
          .toList();
```
#### Sort contacts by name, then by age
```
List<Contact> contactNames = 
      stream(contacts)
          .orderBy(c -> c.getName())
          .thenBy(c -> c.Age())
          .toList();
```
#### Group products by category
```
Map<Category, Stream<Product>> productsByCategory
      stream(products)
          .groupBy(p -> p.getCategory())
          .toMap(g -> g.getKey() /* Category */, g.getElements() /* Stream<Product> */)
```
#### Calculate the total price of a purchase
```
double total = 
      stream(purchase.getItems())
          .sum((Purchase p) -> p.getPrice());
```

There are many more methods: first(), single(), distinct(), any(), aggregate(), count(), take(), skip() and reverse() are all available. Have fun!

## Pull Requests Are Welcome!

Please, send feedback and pull requests a plenty! 
If you find a bug, a missing feature or have an improvement suggestion, don't be afraid to file an issue and we will do our best to attend it.
