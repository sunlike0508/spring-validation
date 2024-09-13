# 검증

## BingingResult

스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기 보관

`BindingResult` 가 있으면 `@ModelAttribute` 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다!



**예) @ModelAttribute에 바인딩 시 타입 오류가 발생하면?**

`BindingResult` 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다. 

`BindingResult` 가 있으면 오류 정보( `FieldError` )를 `BindingResult` 에 담아서 컨트롤러를 정상 호출한다.

**BindingResult에 검증 오류를 적용하는 3가지 방법**

`@ModelAttribute` 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 `FieldError` 생성해서`BindingResult  에 넣어준다.

개발자가 직접 넣어준다.

`Validator`

**타입 오류 확인**

숫자가 입력되어야 할 곳에 문자를 입력해서 타입을 다르게 해서 `BindingResult` 를 호출하고 `bindingResult` 의 값을 확인해보자.

**주의**

`BindingResult` 는 검증할 대상 바로 다음에 와야한다. 순서가 중요하다. 

예를 들어서 `@ModelAttribute Item item` , 바로 다음에 `BindingResult` 가 와야 한다.

`BindingResult` 는 Model에 자동으로 포함된다.

**BindingResult와 Errors** 

`org.springframework.validation.Errors` 

`org.springframework.validation.BindingResult `

`BindingResult` 는 인터페이스이고, `Errors` 인터페이스를 상속받고 있다.

실제 넘어오는 구현체는 `BeanPropertyBindingResult` 라는 것인데, 둘다 구현하고 있으므로 `BindingResult` 대신에 `Errors` 를 사용해도 된다. 

`Errors` 인터페이스는 단순한 오류 저장과 조회 기능을 제공한다. 

`BindingResult` 는 여기에 더해서 추가적인 기능들을 제공한다. 

`addError()` 도 `BindingResult` 가 제공하므로 여기서는 `BindingResult` 를 사용하자. 

주로 관례상 `BindingResult` 를 많이 사용한다.


