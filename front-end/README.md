# Front-end

## TODO

### Custom audio player

- It'd be nice to use a custom audio player with more controls and functionalities that the default HTML one.
- See these links for custom audio players in Javascript
  - https://github.com/nickjj/runninginproduction.com/pull/5/files#diff-068c508b5bcee313b214aff222ab1117404e1497322f2c54f629d94415b55bf2

- Audio buffering feedback
  - https://developer.mozilla.org/en-US/docs/Web/Media/Guides/Audio_and_video_delivery/buffering_seeking_time_ranges

## Documentation on selected topics

### Typescript's `any` vs `unknown`

Okay, let's break down the difference between `unknown` and `any` in TypeScript. They both deal with values whose types aren't fixed, but they approach type safety very differently.

**`any`**

* **The "Opt-Out" Type:** `any` essentially tells the TypeScript compiler, "Don't worry about the type of this value. Assume I know what I'm doing."
* **No Type Safety:** You can do *anything* with a value of type `any` without TypeScript complaining at compile time:
  * Access any property (`value.foo`)
  * Call it like a function (`value()`)
  * Assign it to a variable of any other type (`const num: number = value;`)
* **Risks:** This bypasses TypeScript's core benefit – static type checking. Errors related to incorrect type usage will likely only surface at runtime, potentially causing unexpected crashes or bugs.
* **When to Use (Sparingly):**
  * **Migration:** When gradually migrating a JavaScript codebase to TypeScript, you might use `any` temporarily.
  * **Third-party Libraries:** When working with libraries that don't have proper type definitions (though `unknown` is often a safer choice even here).
  * **True Dynamicism (Rare):** In highly dynamic situations where the type structure genuinely cannot be known or predicted (often a sign of a potential design issue).
  * **Explicit Opt-Out:** As a *very conscious decision* to bypass type checking for a specific, justified reason.

**`unknown`**

* **The "Type-Safe Unknown":** `unknown` represents a value whose type is not known *yet*. It tells the compiler, "This value could be anything, so you must verify its type before using it."
* **Enforces Type Safety:** You can't do much with an `unknown` value directly. TypeScript forces you to narrow down its type before performing operations:
  * You **cannot** access arbitrary properties (`value.foo` will cause a compile error).
  * You **cannot** call it like a function (`value()` will cause a compile error).
  * You **cannot** assign it to a variable with a more specific type without a type check or assertion (`const num: number = value;` will cause a compile error).
* **How to Use:** Before using an `unknown` value, you *must* perform checks:
  * `typeof` checks: `if (typeof value === 'string') { console.log(value.toUpperCase()); }`
  * `instanceof` checks: `if (value instanceof Date) { console.log(value.toISOString()); }`
  * Type Assertions (use with caution): `const num = value as number;` or `<number>value;` (only do this if you are *certain* about the type).
  * Custom Type Guards: Functions that return a type predicate (`value is MyType`).
* **When to Use (Preferred):**
  * **API Responses:** Data fetched from external APIs often has a structure that isn't guaranteed. Fetching into `unknown` and then validating is safe.
  * **User Input:** Values from forms or other user inputs.
  * **Dynamic Data:** When working with configuration files or data loaded from sources where the type isn't guaranteed.
  * **Safer Alternative to `any`:** Whenever you might be tempted to use `any` because you don't know the type, `unknown` is usually the better, safer choice because it forces you to handle the uncertainty explicitly.

**Here's a table summarizing the key differences:**

| Feature                  | `any`                                   | `unknown`                                        |
|:-------------------------|:----------------------------------------|:-------------------------------------------------|
| **Type Safety**          | **No** (Bypasses type checker)          | **Yes** (Forces type checking before use)        |
| **Operations Allowed**   | Anything (property access, calls, etc.) | Almost none (assignment to `any`/`unknown` okay) |
| **Assignment To**        | Any type                                | Only `any` or `unknown`                          |
| **Assignment From**      | Any type                                | Any type                                         |
| **Requires Type Checks** | No                                      | **Yes** (before performing most operations)      |
| **Primary Goal**         | Opt-out of type checking                | Safely handle values of unknown type             |

**In short:**

* Use `any` when you need to **bypass** type checking (use as a last resort).
* Use `unknown` when you have a value whose type isn't known upfront, and you want TypeScript to **enforce** safe handling of that value.

**Recommendation:** Always prefer `unknown` over `any` whenever possible. It leads to safer, more maintainable code by forcing you to address type uncertainty explicitly rather than ignoring it.

### How do `any` and `unknown` compare to `never`

**`never`**

- **The "Impossible" Type:** `never` represents the type of values that **never occur**. It indicates that a certain state or code path is impossible or should never be reached.
- **No Values:** There is no value that has the type `never`. You cannot assign _any_ value (not even `null` or `undefined`) to a variable of type `never`, except `never` itself (which you can't produce anyway).
- **Use Cases:**
  - **Functions That Never Return:** A function that _always_ throws an error or enters an infinite loop has a return type of `never` because it never successfully returns a value.

``` typescript
function throwError(message: string): never {
  throw new Error(message);
}

function infiniteLoop(): never {
  while (true) {}
}
```

- **Exhaustiveness Checking:** In type narrowing (like `if`/`else` or `switch` statements), if you've covered all possible cases of a union type, the type of the variable in the final `else` or `default` block might narrow down to `never`. This is useful for ensuring you've handled every possibility.

``` typescript
type Shape = "circle" | "square";

function getArea(shape: Shape): number {
  switch (shape) {
    case "circle":
      return Math.PI * 5 ** 2;
    case "square":
      return 10 * 10;
    default:
      // If we add a new shape to the `Shape` union later,
      // and forget to add a case here, `_exhaustiveCheck`
      // will have a non-`never` type, causing a compile error.
      const _exhaustiveCheck: never = shape;
      return _exhaustiveCheck;
  }
}
```

- **Impossible Intersections:** Sometimes, type manipulations or conditional types might result in a type that logically cannot exist, which TypeScript represents as `never`.
- **Assignability:**
  - `never` is assignable to **every** other type (`string`, `number`, `any`, `unknown`, etc.). This sounds weird, but it makes sense: since a value of type `never` can never occur, assigning it doesn't violate the type rules of any other type. It's vacuously true.
  - **Nothing** (except `never` itself) is assignable to `never`.

**Comparison Table:**

| Feature                  | `any`                           | `unknown`                                         | `never`                                                   |
|--------------------------|---------------------------------|---------------------------------------------------|-----------------------------------------------------------|
| **Represents**           | Any value (opt-out of checking) | A value whose type is not known (requires checks) | A value that **never** occurs (impossible state)          |
| **Type Safety**          | **No** (Bypasses type checker)  | **Yes** (Forces type checking before use)         | **Yes** (Represents impossibility/unreachability)         |
| **Holds Values?**        | Yes, any value                  | Yes, any value                                    | **No**, holds no values                                   |
| **Operations Allowed**   | Anything                        | Almost none (without type checks)                 | N/A (Cannot have a value)                                 |
| **Assignment To**        | Any type                        | Only `any` or `unknown`                           | **Any type**                                              |
| **Assignment From**      | Any type                        | Any type                                          | **Only `never`** (effectively nothing)                    |
| **Requires Type Checks** | No                              | **Yes** (before most operations)                  | N/A                                                       |
| **Primary Purpose**      | Opt-out of type checking        | Safely handle values of unknown type              | Represent unreachable code paths or impossible conditions |

**In Summary:**

- `any`: **Unsafe**. Use it to tell TypeScript "don't check this". Avoid if possible.
- `unknown`: **Safe**. Use it for values where the type isn't known beforehand. Forces you to check the type before use. Prefer this over `any`.
- `never`: **Represents Impossibility**. Use it (or recognize it) for functions that don't return, for exhaustiveness checks, or to signify states that cannot logically occur. It's a bottom type, assignable to anything, but nothing can be assigned to it.
