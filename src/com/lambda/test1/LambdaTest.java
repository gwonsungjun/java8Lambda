package com.lambda.test1;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaTest {

    static int outerStaticNum;

    public static void main(String[] args) {

        // 기존의 정렬방식
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

        for (String name : names) {
            System.out.println(name);
        }
        dashPrit();

        // 정렬할 List와 Comparator를 받아서 정렬
        // (익명의 comparator를 생성해서 정렬하기 위한 기준으로 줘야 했음)
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        // 자바 8 람다식 사용
        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });

        // body code가 한줄이면, 중괄호와 return 키워드를 생략할 수 있음.
        Collections.sort(names, (String a, String b) -> b.compareTo(a));

        // 컴파일러 타입 추론때문에 생략 가능
        Collections.sort(names, (a, b) -> b.compareTo(a));

        for (String name : names) {
            System.out.println(name);
        }

        dashPrit();
        Convert<String, Integer> convert = (from) -> Integer.valueOf(from);
        Integer converted = convert.convert("123");
        System.out.println(converted);

        // 자바 8 :: 기호를 통한 method나 생성자의 레퍼런스를 넘겨 줄 수 있음.
        dashPrit();
        Convert<String, Integer> converter = Integer::valueOf;
        Integer converted2 = converter.convert("123");
        System.out.println(converted2);

        dashPrit();
        PersonFactory personFactory = Person::new;
        Person person = personFactory.create("peter", "parker");
        System.out.println(person.getFirstName());
        System.out.println(person.getLastName());

        // 람다 scope Test
        int outerNum = 1;
        // outerNum 재정의 & 선언 불가능. 결과적으로 컴파일 에러
       /* Convert<Integer, String> stringConverter1 = (from) -> {
            outerNum = 23;
            return String.valueOf(from);
        };*/

        // 지역변수와 달리 필드값 & static한 변수는 람다식 내에서 읽고 쓰기 가능
        Convert<Integer, String> stringConverter2 = (from) -> {
            outerStaticNum = 72;
            return String.valueOf(from);
        };

        // Built-in Functional Interface (Predicate, Function, Supplier, Consumer 인터페이스)
        dashPrit();
        Predicate<String> predicate = (s) -> s.length() > 0;

        boolean val = predicate.test("foo");
        System.out.println(val);
        val = predicate.negate().test("foo");
        System.out.println(val);

        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;
        Predicate<String> isEmpty = String::isEmpty;

        Function<String, Integer> toInteger = Integer::valueOf;
        //String -> int -> String, andThen 체이닝
        Function<String, String> backToString = toInteger.andThen(String::valueOf);

        Supplier<Person> personSupplier = Person::new;
        personSupplier.get(); // new Persion

        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
        greeter.accept(new Person("Luke", "skywalker"));
        backToString.apply("123");

        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");

        comparator.compare(p1, p2);
        comparator.reversed().compare(p1, p2);

        // null이 될 수도, 혹은 null 아닌 값이 될 수도 있는 것을 다루는 간단한 컨테이너
        Optional<String> optional = Optional.of("bam");
        optional.isPresent(); // true
        optional.get(); // "bam"
        optional.orElse("fallback"); // "bam"

        optional.ifPresent((s) -> System.out.println(s.charAt(0))); // "b"


        //Stream
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        // sorted stream을 return 하기때문에 중간에 사용, 따로 comparator 안주면 natural order (1, 2, 3, ...) 정렬
        //filter는 predicate를 받아서 필터링, stream을 return 하기 때문에 중간에만 낄 수 있음.
        //forEach는 스트림의 각 원소에 대해 적용가능한 consumer를 받아서 적용, foreach는 마지막에 사용, 리턴값이 void,, 따라서 체이닝 불가
        stringCollection
                .stream()
                .sorted()
                .filter((s) -> s.startsWith("a"))
                .forEach(System.out::println); // "aaa2", "aaa1"

        // 실제 sorted 메소드는 스트림에 있는 원소들을 정렬하는 것이 아니라, 정렬된 view를 제공
        // 따라서 아래 결과는 정렬이 안된 그대로 출력됨.
        // map 역시 스트림을 리턴 따라서 중간에 끼워서 사용하는 메소드, 주어진 함수를 스트림의 각 원소에  mapping
        stringCollection
                .stream()
                .map(String::toUpperCase)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(System.out::println);

        // match method : predicate를 받아서 stream에 match되는지 확인, boolean형 return 따라서 마지막에 사용
        boolean anyStratsWithA = stringCollection
                .stream()
                .anyMatch((s) -> s.startsWith("a"));
        System.out.println(anyStratsWithA);

        boolean allStratsWithA = stringCollection
                .stream()
                .allMatch((s) -> s.startsWith("a"));
        System.out.println(allStratsWithA);

        boolean noneStartsWithX = stringCollection
                .stream()
                .noneMatch((s) -> s.startsWith("z"));
        System.out.println(noneStartsWithX);

        //count : return long
        long startsWithB = stringCollection
                .stream()
                .filter((s) -> s.startsWith("b"))
                .count();
        System.out.println(startsWithB);

        //reduce : reduction한 값을 optional로 return
        Optional<String> reduced = stringCollection
                .stream()
                .sorted()
                .reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(System.out::println);

        //Parallel Stream
        // stream은 순차적 or 병렬적으로 수행 가능 (전자는 싱글 스레드, 후자는 멀티 스레드에서 동시 수행)
        int max = 1000000;
        List<String> values = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        // 싱글 스레드
        long t0 = System.nanoTime();

        long count = values.stream().sorted().count();
        System.out.println(count);

        long t1 = System.nanoTime();

        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("sequential sort took: %d ms", millis)); // 899ms

        //멀티 스레드
        long s0 = System.nanoTime();

        long count2 = values.parallelStream().sorted().count();
        System.out.println(count2);

        long s1 = System.nanoTime();

        long millis2 = TimeUnit.NANOSECONDS.toMillis(s1 - s0);
        System.out.println(String.format("sequential sort took: %d ms", millis2)); // 388ms : 수행시간 반토막

        // Map  stream 지원 x
        // putIfAbsent : 없으면 넣겠다.
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }
        map.forEach((id, val2) -> System.out.println(val2));

        // key-vaule가 일치할 경우에만 삭제
        map.remove(3, "val3");
        map.get(3);  // val33

        map.remove(3, "val33");
        map.get(3);  // null

        map.merge(9, "val9", (value, newValue) -> value.concat(newValue));
        map.get(9);  // val9

        map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
        map.get(9);  // val9concat


        // Date API
        // 자바 8 - java.time 밑에 새로운 date와 time API 도입.

        //clock System.currentTimeMillis() 대신 사용
        Clock clock = Clock.systemDefaultZone();
        long milli = clock.millis();

        // Instant 객체를 통한 java.util.Date 객체 얻을 수 있음
        Instant instant = clock.instant();
        Date legacyDate = Date.from(instant);

        // prints all available timezone ids
        System.out.println(ZoneId.getAvailableZoneIds());

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        System.out.println(zone1.getRules());
        System.out.println(zone2.getRules());

        LocalTime now1 = LocalTime.now(zone1);
        LocalTime now2 = LocalTime.now(zone2);

        System.out.println(now1.isBefore(now2)); //false

        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

        System.out.println(hoursBetween); // -4
        System.out.println(minutesBetween); // -239

    }

    @FunctionalInterface
    interface Convert<F, T> {
        T convert(F from);
    }

    @FunctionalInterface
    interface PersonFactory<P extends Person> {
        P create(String firstName, String lastName);

    }

    public static void dashPrit() {
        System.out.println("------------------------------");
    }

}
