# Payabli Java Library

[![fern shield](https://img.shields.io/badge/%F0%9F%8C%BF-Built%20with%20Fern-brightgreen)](https://buildwithfern.com?utm_source=github&utm_medium=github&utm_campaign=readme&utm_source=https%3A%2F%2Fgithub.com%2Fpayabli%2Fsdk-java)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.payabli/sdk-java)](https://central.sonatype.com/artifact/io.github.payabli/sdk-java)

The Payabli Java library provides convenient access to the Payabli APIs from Java.

## Usage

Instantiate and use the client with the following:

```java
package com.example.usage;

import io.github.payabli.api.PayabliApiClient;
import io.github.payabli.api.resources.moneyin.requests.RequestPayment;
import io.github.payabli.api.resources.moneyin.types.TransRequestBody;
import io.github.payabli.api.types.PayMethodCredit;
import io.github.payabli.api.types.PaymentDetail;
import io.github.payabli.api.types.PaymentMethod;

public class Example {
    public static void main(String[] args) {
        PayabliApiClient client = PayabliApiClient
            .builder()
            .apiKey("<value>")
            .build();

        client.moneyIn().getpaid(
            RequestPayment
                .builder()
                .body(
                    TransRequestBody
                        .builder()
                        .paymentDetails(
                            PaymentDetail
                                .builder()
                                .totalAmount(1.1)
                                .build()
                        )
                        .paymentMethod(
                            PaymentMethod.ofPayMethodCredit(
                                PayMethodCredit
                                    .builder()
                                    .cardexp("alpha")
                                    .cardnumber("cardnumber")
                                    .method("card")
                                    .build()
                            )
                        )
                        .build()
                )
                .build()
        );
    }
}
```

## Environments

This SDK allows you to configure different environments for API requests.

```java
import io.github.payabli.api.PayabliApiClient;
import io.github.payabli.api.core.Environment;

PayabliApiClient client = PayabliApiClient
    .builder()
    .environment(Environment.Sandbox)
    .build();
```

## Base Url

You can set a custom base URL when constructing the client.

```java
import io.github.payabli.api.PayabliApiClient;

PayabliApiClient client = PayabliApiClient
    .builder()
    .url("https://example.com")
    .build();
```

## Exception Handling

When the API returns a non-success status code (4xx or 5xx response), an API exception will be thrown.

```java
import io.github.payabli.api.core.PayabliApiApiException;

try {
    client.moneyIn().getpaid(...);
} catch (PayabliApiApiException e) {
    // Do something with the API exception...
}
```

## Advanced

### Custom Client

This SDK is built to work with any instance of `OkHttpClient`. By default, if no client is provided, the SDK will construct one. 
However, you can pass your own client like so:

```java
import io.github.payabli.api.PayabliApiClient;
import okhttp3.OkHttpClient;

OkHttpClient customClient = ...;

PayabliApiClient client = PayabliApiClient
    .builder()
    .httpClient(customClient)
    .build();
```

### Retries

The SDK is instrumented with automatic retries with exponential backoff. A request will be retried as long
as the request is deemed retryable and the number of retry attempts has not grown larger than the configured
retry limit (default: 2).

A request is deemed retryable when any of the following HTTP status codes is returned:

- [408](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/408) (Timeout)
- [429](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/429) (Too Many Requests)
- [5XX](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/500) (Internal Server Errors)

Use the `maxRetries` client option to configure this behavior.

```java
import io.github.payabli.api.PayabliApiClient;

PayabliApiClient client = PayabliApiClient
    .builder()
    .maxRetries(1)
    .build();
```

### Timeouts

The SDK defaults to a 60 second timeout. You can configure this with a timeout option at the client or request level.

```java
import io.github.payabli.api.PayabliApiClient;
import io.github.payabli.api.core.RequestOptions;

// Client level
PayabliApiClient client = PayabliApiClient
    .builder()
    .timeout(10)
    .build();

// Request level
client.moneyIn().getpaid(
    ...,
    RequestOptions
        .builder()
        .timeout(10)
        .build()
);
```

### Custom Headers

The SDK allows you to add custom headers to requests. You can configure headers at the client level or at the request level.

```java
import io.github.payabli.api.PayabliApiClient;
import io.github.payabli.api.core.RequestOptions;

// Client level
PayabliApiClient client = PayabliApiClient
    .builder()
    .addHeader("X-Custom-Header", "custom-value")
    .addHeader("X-Request-Id", "abc-123")
    .build();
;

// Request level
client.moneyIn().getpaid(
    ...,
    RequestOptions
        .builder()
        .addHeader("X-Request-Header", "request-value")
        .build()
);
```

## Contributing

While we value open-source contributions to this SDK, this library is generated programmatically.
Additions made directly to this library would have to be moved over to our generation code,
otherwise they would be overwritten upon the next generated release. Feel free to open a PR as
a proof of concept, but know that we will not be able to merge it as-is. We suggest opening
an issue first to discuss with us!

On the other hand, contributions to the README are always very welcome!
## Installation

### Gradle

Add the dependency in your `build.gradle` file:

```groovy
dependencies {
  implementation 'io.github.payabli:sdk-java'
}
```

### Maven

Add the dependency in your `pom.xml` file:

```xml
<dependency>
  <groupId>io.github.payabli</groupId>
  <artifactId>sdk-java</artifactId>
  <version>0.0.292</version>
</dependency>
```

## Reference

A full reference for this library is available [here](https://github.com/payabli/sdk-java/blob/HEAD/./reference.md).
