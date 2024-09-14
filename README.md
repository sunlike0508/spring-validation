# 검증

## BingingResult

스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기 보관

`BindingResult` 가 있으면 `@ModelAttribute` 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다!



**예) @ModelAttribute에 바인딩 시 타입 오류가 발생하면?**

`BindingResult` 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다. 

`BindingResult` 가 있으면 오류 정보( `FieldError` )를 `BindingResult` 에 담아서 컨트롤러를 정상 호출한다.

## BindingResult에 검증 오류를 적용하는 3가지 방법

`@ModelAttribute` 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 `FieldError` 생성해서`BindingResult  에 넣어준다.

개발자가 직접 넣어준다.

`Validator`

**타입 오류 확인**

숫자가 입력되어야 할 곳에 문자를 입력해서 타입을 다르게 해서 `BindingResult` 를 호출하고 `bindingResult` 의 값을 확인해보자.

**주의**

`BindingResult` 는 검증할 대상 바로 다음에 와야한다. 순서가 중요하다. 

예를 들어서 `@ModelAttribute Item item` , 바로 다음에 `BindingResult` 가 와야 한다.

`BindingResult` 는 Model에 자동으로 포함된다.

## BindingResult와 Errors 

`org.springframework.validation.Errors` 

`org.springframework.validation.BindingResult `

`BindingResult` 는 인터페이스이고, `Errors` 인터페이스를 상속받고 있다.

실제 넘어오는 구현체는 `BeanPropertyBindingResult` 라는 것인데, 둘다 구현하고 있으므로 `BindingResult` 대신에 `Errors` 를 사용해도 된다. 

`Errors` 인터페이스는 단순한 오류 저장과 조회 기능을 제공한다. 

`BindingResult` 는 여기에 더해서 추가적인 기능들을 제공한다. 

`addError()` 도 `BindingResult` 가 제공하므로 여기서는 `BindingResult` 를 사용하자. 

주로 관례상 `BindingResult` 를 많이 사용한다.


## FieldError 생성자

`FieldError` 는 두 가지 생성자를 제공한다.

```java
 public FieldError(String objectName, String field, String defaultMessage);
 public FieldError(String objectName, String field, @Nullable Object
 rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable
 Object[] arguments, @Nullable String defaultMessage);
```

파라미터 목록

`objectName` : 오류가 발생한 객체 이름

`field` : 오류 필드

`rejectedValue` : 사용자가 입력한 값(거절된 값)

`bindingFailure` : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값 `codes` : 메시지 코드

`arguments` : 메시지에서 사용하는 인자

`defaultMessage` : 기본 오류 메시지


```java

bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 9999개까지 입니다"));

```

`ObjectError` 도 유사하게 두 가지 생성자를 제공한다. 코드를 참고하자.

### rejectValue()

```java
void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```

`field` : 오류 필드명

`errorCode` : 오류 코드(이 오류 코드는 메시지에 등록된 코드가 아니다. 뒤에서 설명할 messageResolver를 위한 오류 코드이다.)

`errorArgs` : 오류 메시지에서 `{0}` 을 치환하기 위한 값

`defaultMessage` : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

### reject()

```java
void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```


**축약된 오류 코드**

`FieldError()` 를 직접 다룰 때는 오류 코드를 `range.item.price` 와 같이 모두 입력했다. 

그런데 `rejectValue()` 를 사용하고 부터는 오류 코드를 `range` 로 간단하게 입력했다. 

그래도 오류 메시지를 잘 찾아서 출력한다. 이 부분을 이해하려면 `MessageCodesResolver` 를 이해해야 한다.

```properties
#Level1
required.item.itemName: 상품 이름은 필수 입니다. #Level2
required: 필수 값 입니다.
```
물론 이렇게 객체명과 필드명을 조합한 메시지가 있는지 우선 확인하고, 없으면 좀 더 범용적인 메시지를 선택하도록 추가 개발을 해야겠지만, 범용성 있게 잘 개발해두면, 메시지의 추가 만으로 매우 편리하게 오류 메시지를 관리할 수 있을 것이다.


## MessageCodesResolver

검증 오류 코드로 메시지 코드들을 생성한다.

`MessageCodesResolver` 인터페이스이고 `DefaultMessageCodesResolver` 는 기본 구현체이다. 

주로 다음과 함께 사용 `ObjectError` , `FieldError`

### DefaultMessageCodesResolver의 기본 메시지 생성 규칙

```java
MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();
```
**동작 방식**

`rejectValue()` , `reject()` 는 내부에서 `MessageCodesResolver` 를 사용한다. 여기에서 메시지 코드들
을 생성한다.

`FieldError` , `ObjectError` 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다. 

`MessageCodesResolver` 를 통해서 생성된 순서대로 오류 코드를 보관한다.

이 부분을 `BindingResult` 의 로그를 통해서 확인해보자.

`codes [range.item.price, range.price, range.java.lang.Integer, range]`

**FieldError** 

`rejectValue("itemName", "required")` 다음 4가지 오류 코드를 자동으로 생성

`required.item.itemName` `required.itemName` `required.java.lang.String` `required`

**ObjectError** 

`reject("totalPriceMin")` 다음 2가지 오류 코드를 자동으로 생성

`totalPriceMin.item` `totalPriceMin`

## 오류코드 관리 전략

핵심은 구체적인 것에서 공통적인 것으로

크게 중요하지 않은 것은 공통적인 것으로 하는 것이 정신건강(?)에 좋음


## Validator

```java
public interface Validator {
     boolean supports(Class<?> clazz);
     void validate(Object target, Errors errors);
}
```

`supports() {}` : 해당 검증기를 지원하는 여부 확인.

`validate(Object target, Errors errors)` : 검증 대상 객체와 `BindingResult`

```java
@InitBinder
public void init(WebDataBinder dataBinder) {
    log.info("init binder {}", dataBinder);
    dataBinder.addValidators(itemValidator);
}

@PostMapping("/add")
public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes);
```

이렇게 `WebDataBinder` 에 검증기를 추가하면 해당 컨트롤러에서는 검증기를 자동으로 적용할 수 있다.

`@InitBinder` 해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다.

검증 대상 앞에 `@Validated` 를 붙인다.

**글로벌 설정**

```java
@SpringBootApplication
public class ItemServiceApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class, args);
    }
    @Override
    public Validator getValidator() {
        return new ItemValidator();
    }
}

```

# Bean Validation

먼저 Bean Validation은 특정한 구현체가 아니라 Bean Validation 2.0(JSR-380)이라는 기술 표준이다. 

쉽게 이야 기해서 검증 애노테이션과 여러 인터페이스의 모음이다. 

마치 JPA가 표준 기술이고 그 구현체로 하이버네이트가 있는 것과 같다.

`@NotBlank` : 빈값 + 공백만 있는 경우를 허용하지 않는다.

`@NotNull` : `null` 을 허용하지 않는다.

`@Range(min = 1000, max = 1000000)` : 범위 안의 값이어야 한다. 

`@Max(9999)` : 최대 9999까지만 허용한다.