# ICU4J

`ICU4J` provides improvements over `JDK` internationalization capabilities and is highly recommended
for localizing a Java application.

The "ICU" message format pattern have been adopted by many other libraries in a variety of
languages.

This guide covers the most common usages and showcases some differences between languages when it
comes to formatting numbers and dates as well as handling plurals and gender in messages.

Check the [installation guides](installation) to add the dependency and the
[ICU documentation](https://unicode-org.github.io/icu/userguide/format_parse/messages/) for more
information.

## Basic formatting

`ICU4J` support both named and numbered arguments (see
[differences with JDK](fluent-api#named-vs-numbered-arguments)).

`L10nMessages` always references placeholders by their name as a "string". A numbered argument `{0}`
is referenced by the key `"0"`.

:::note

It is recommended to always use named arguments since it is self documenting and gives more context
to translators, leading to better translations.
:::

```properties title=en.properties
welcome_user=Welcome {username}!
welcome_user_numbered=Welcome {0}!
```

```properties title=fr.properties
welcome_user_numbered=Bienvenue {username}!
```

```java
class Example {

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    System.out.println(m.format(welcome_user, "username", "Mary"));
    // Welcome Mary!

    System.out.println(m.format(welcome_user_numbered, "0", "Mary"));
    // Welcome Mary!
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    System.out.println(m.format(welcome_user, "username", "Mary"));
    // Bienvenue Mary!
  }
}
```

## Numbers

For number formatting, use the `number` argument. Optionally, add a pre-defined `argStyle` such as
`integer`, `percent`, `currency` to customize output.

```properties title=en.properties
numbers=Examples: {basic, number}, {basic, number, integer}, {percentage, number, percent}, \
  {currency, number, currency}
```

```properties title=fr.properties
numbers=Exemples: {basic, number}, {basic, number, integer}, {percentage, number, percent}, \
  {currency, number, currency}
```

```java
class Example {

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    System.out.println(m.format(numbers, "basic", 1000, "percentage", 0.5, "currency", 50));
    // Examples: 1,000.1, 1,000, 50%, $50.00
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    System.out.println(m.format(numbers, "basic", 1000, "percentage", 0.5, "currency", 50));
    // Exemples: 1 000,1, 1 000, 50 %, 50,00 €
  }
}
```

## Dates and Times

For date formatting, use the `date` argument and for time formatting the `time` argument.
Optionally, add pre-defined `argStyle` such as `short`, `medium`, `long`, `full` to customize
output.

```properties title=en.properties
today_is=Today is: {todayDate, date}, it is: {todayDate, time}
today_is_short=Today is: {todayDate, date, short}, it is: {todayDate, time, short}
```

```properties title=fr.properties
today_is=Aujourd'hui c'est le: {todayDate, date}, il est: {todayDate, time}
today_is_short=Aujourd'hui c'est le: {todayDate, date, short}, il est: {todayDate, time, short}
```

```java
class Example {

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    System.out.println(m.format(today_is, "todayDate", new Date()));
    // Today is: Jun 22, 2022, it is: 8:36:08 PM

    System.out.println(m.format(today_is_short, "todayDate", new Date()));
    // Today is: 6/22/22, it is: 8:36 PM
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    System.out.println(m.format(today_is, "todayDate", new Date()));
    // Aujourd'hui c'est le: 22 juin 2022, il est: 20:36:08

    System.out.println(m.format(today_is_short, "todayDate", new Date()));
    // Aujourd'hui c'est le: 22/06/2022, il est: 20:36
  }
}
```

## Skeletons

Further customization for number, date and time formatting can be done using skeletons. Here is just
a basic example to show the syntax. For more information, see
[number skeletons](https://unicode-org.github.io/icu/userguide/format_parse/numbers/skeletons.html)
and [date skeletons](https://unicode-org.github.io/icu/userguide/format_parse/datetime/).

```properties title=en.properties
skeletons={number, number, :: .00 currency/CAD} - {date, date, :: MMMMdjmm}
```

```java
class Example {

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    System.out.println(m.format(skeletons, "number", 1000.1234, "date", new Date()));
    // CA$1,000.12 - June 22, 9:29:42 PM
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    System.out.println(m.format(skeletons, "number", 1000.1234, "date", new Date()));
    // 1 000,12 $CA - 22 juin, 21:29:42
  }
}
```

## Pluralization - Message with quantity

Consider the following sentence: `You have 5 messages` where the quantity: `5` varies at runtime:
`0, 1, 5, 100, etc`.

The ending of the noun `message` will change based on the quantity:

- `You have 0 messages`
- `You have 1 message`
- `You have 5 messages`
- `You have 100 messages`

From this, we can identify the 2 forms (singular and plural) needed to render all variations:

- `You have {numberMessage} message`
- `You have {numberMessage} messages`

Sometimes, it is also preferable to customize the output for a specific quantity, typically `0`. For
example, `You have no messages` reads better than `You have 0 messages`.

The `plural` argument is a special construct provided by `ICU` to list the different plural forms of
a message in a single string and to customize them based on the language. It also has a mechanism to
specify a message for a given quantity.

Many languages will need adaptation to the number of plural forms. Some require a single form only,
while others may need up to six forms to cover all the linguistic requirements. The mapping from
quantity to plural form is performed by the library at runtime based on the
[CLDR plural rules](https://unicode-org.github.io/cldr-staging/charts/latest/supplemental/language_plural_rules.html)
.

French uses 2 forms like English but the singular is used for `0`

- `Vous avez 0 message`
- `Vous avez 1 message`
- `Vous avez 5 messages`

Japanese uses a single form:

- `メッセージが 0 件`
- `メッセージが 1 件`
- `メッセージが 5 件`

Russian needs 4 forms and is cyclic:

- `У вас есть 0 сообщений`
- `У вас есть 1 сообщение`
- `У вас есть 2 сообщения`
- `У вас есть 5 сообщений`
- `У вас есть 21 сообщение`
- `У вас есть 22 сообщения`
- `У вас есть 25 сообщений`

:::info

A common pitfall is to skip pluralization when the quantity is know to be greater than one. While
this work in English, it will prevent proper localization in some other languages like Russian.

Always use a `plural` argument when working with a variable quantity.
:::

### Mapping to CLDR forms

When writing the source string in English, the singular string `You have {numberMessage} message`
maps to CLDR's `one` form and the plural string `You have {numberMessage} messages` maps to CLDR's
`other` form.

Some languages like Japanese will use a single form (`other`) in the localized message, whereas
Russian will use 4 forms (`one`, `few`, `many` and `other`) and Arabic will use six forms (`zero`,
`one`, `two`, `few`, `many` and `other`).

See [CLDR Plural Rules](https://cldr.unicode.org/index/cldr-spec/plural-rules) for more details.

```properties title=en.properties
you_have_messages={numberMessages, plural, \
  one   {You have {numberMessages} message} \
  other {You have {numberMessages} messages}}
```

```properties title=fr.properties
you_have_messages={numberMessages, plural, \
  one   {Vous avez {numberMessages} message} \
  other {Vous avez {numberMessages} messages}}
```

```properties title=ja.properties
you_have_messages={numberMessages, plural, \
  other {メッセージが {numberMessages} 件}}
```

```properties title=ru.properties
you_have_messages={numberMessages, plural, \
  one   {У вас есть {numberMessages} сообщение} \
  few   {У вас есть {numberMessages} сообщения} \
  many  {У вас есть {numberMessages} сообщений} \
  other {У вас есть {numberMessages} сообщения}}
```

```java
class Example {

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    IntStream.range(0, 3)
        .forEach(
            numberMessages ->
                System.out.println(m.format(you_have_messages, "numberMessages", numberMessages)));

    // You have 0 messages
    // You have 1 message
    // You have 2 messages
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    IntStream.range(0, 3)
        .forEach(
            numberMessages ->
                System.out.println(m.format(you_have_messages, "numberMessages",
                    numberMessages)));

    // Vous avez 0 message
    // Vous avez 1 message
    // Vous avez 2 messages
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.JAPANESE).build();
    IntStream.range(0, 3)
        .forEach(
            numberMessages ->
                System.out.println(m.format(you_have_messages, "numberMessages",
                    numberMessages)));

    // メッセージが 0 件
    // メッセージが 1 件
    // メッセージが 2 件
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class)
        .locale(Locale.forLanguageTag("ru")).build();
    IntStream.of(0, 1, 2, 5, 21, 22, 25)
        .forEach(
            numberMessages ->
                System.out.println(m.format(you_have_messages, "numberMessages",
                    numberMessages)));

    // У вас есть 0 сообщений
    // У вас есть 1 сообщение
    // У вас есть 2 сообщения
    // У вас есть 5 сообщений
    // У вас есть 21 сообщение
    // У вас есть 22 сообщения
    // У вас есть 25 сообщений
  }
}
```

:::info

The `other` form must always be provided. By default, `L10nMessages` ensures it is the case as part
of the message format validation.

:::

### Customized message for a specific quantity

It is possible to provide a customized message for a specific quantity. For example, use `=0` to
provide the specific message: `You have no messages` when the number of messages is `0`.

```properties title=en.properties
you_have_messages={numberMessage, plural, \
  =0    {You have no messages} \
  one   {You have {numberMessage} message} \
  other {You have {numberMessage} messages}}
```

```properties title=fr.properties
you_have_messages={numberMessage, plural, \
  =0    {Vous n'avez aucun message} \
  one   {Vous avez {numberMessage} message} \
  other {Vous avez {numberMessage} messages}}
```

```java
class Example {

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    IntStream.range(0, 3)
        .forEach(
            numberMessage ->
                System.out.println(m.format(you_have_messages, "numberMessage", numberMessage)));

    // You have no messages
    // You have 1 message
    // You have 2 messages
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    IntStream.range(0, 3)
        .forEach(
            numberMessage ->
                System.out.println(m.format(you_have_messages, "numberMessage",
                    numberMessage)));

    // Vous n'avez aucun message
    // Vous avez 1 message
    // Vous avez 2 messages
  }
}
```

## Complex messages - Genderization, list formatting and more

`ICU` provides many other features that are useful to localize messages. Combining them allows
building more complex messages, but the complexity might quickly explode. It may also introduce edge
cases that are not properly translatable in all languages so it is important to keep messages as
simple as possible.

This example shows how to genderize a sentence using the `select` argument and how to use the
[ListFormatter](https://unicode-org.github.io/icu-docs/apidoc/dev/icu4j/com/ibm/icu/text/ListFormatter.Type.html)
formatter that is not directly accessible in the message format. It is combined with the `plural`
argument to handle the empty list and the list with a single element.

:::info

It is recommended to write full sentences, hence to move the arguments to the outer part of the
messages. The `plural` arguments should be nested inside the `select` argument that is used for the
gender.

:::

```properties title=en.properties
favorite_numbers={userGender, select, \
  female {{numbersCount, plural, \
     =0    {She has no favorite numbers} \
     one   {Her favorite number is {numbers}} \
     other {Her favorite numbers are {numbers}}}} \
  male   {{numbersCount, plural, \
     =0    {He has no favorite numbers} \
     one   {His favorite number is {numbers}} \
     other {His favorite numbers are {numbers}}}} \
  other  {{numbersCount, plural, \
     =0    {They have no favorite numbers} \
     one   {Their favorite number is {numbers}} \
     other {Their favorite numbers are {numbers}}}} \
  }
```

```properties title=fr.properties
favorite_numbers={userGender, select, \
  female {{numbersCount, plural, \
     =0    {Elle n'a pas de nombre préferé} \
     one   {Son nombre préferé est {numbers}} \
     other {Ses nombres préferés sont {numbers}}}} \
  male   {{numbersCount, plural, \
     =0    {Il n'a pas de nombre préferé} \
     one   {Son nombre préferé est {numbers}} \
     other {Ses nombres préferés sont {numbers}}}} \
  other  {{numbersCount, plural, \
     =0    {Il/Elle n'a pas de nombre préferé} \
     one   {Son nombre préferé est {numbers}} \
     other {Ses nombres préferés sont {numbers}}}} \
  }
```

```java
class Example {

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();
    ListFormatter lf = ListFormatter.getInstance();
    Stream.of("female", "male", "other").forEach(userGender ->
        Stream.of(Arrays.asList(), Arrays.asList("1"), Arrays.asList("3", "7"))
            .forEach(numbers -> System.out.println(
                m.format(favorite_numbers, "numbers", lf.format(numbers),
                    "numbersCount", numbers.size(), "userGender", userGender))));

    // She has no favorite numbers
    // Her favorite number is 1
    // Her favorite numbers are 3 and 7

    // He has no favorite numbers
    // His favorite number is 1
    // His favorite numbers are 3 and 7

    // They have no favorite numbers
    // Their favorite number is 1
    // Their favorite numbers are 3 and 7
  }

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).locale(Locale.FRANCE).build();
    ListFormatter lf = ListFormatter.getInstance(Locale.FRANCE);
    Stream.of("female", "male", "other").forEach(gender ->
        Stream.of(Arrays.asList(), Arrays.asList("1"), Arrays.asList("3", "7"))
            .forEach(numbers -> System.out.println(
                m.format(favorite_numbers, "numbers", lf.format(numbers),
                    "numbersCount", numbers.size(), "userGender", gender))));

    // Elle n'a pas de nombre préferé
    // Son nombre préferé est 1
    // Ses nombres préferés sont 3 et 7

    // Il n'a pas de nombre préferé
    // Son nombre préferé est 1
    // Ses nombres préferés sont 3 et 7

    // Il/Elle n'a pas de nombre préferé
    // Son nombre préferé est 1
    // Ses nombres préferés sont 3 et 7
  }
}

```
