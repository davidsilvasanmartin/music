# JPA Notes

## 1. Owning side of a relationship

### 1.1 For one-to-many relationships

Let's break down the "owning side" concept with a simple example: **Departments** and **Employees**.
Imagine you have two database tables:

1. **`departments`** table:
    - `id` (Primary Key)
    - `name`

2. **`employees`** table:
    - `id` (Primary Key)
    - `name`
    - `department_id` (Foreign Key referencing `departments.id`)

- An employee belongs to exactly _one_ department.
- A department can have _many_ employees.

Notice where the link (the foreign key `department_id`) is stored: it's in the **`employees`** table. The `employees`
table physically holds the information about which department an employee belongs to.
Now, let's translate this to JPA entities:

**1. The `Employee` Entity (Owning Side):**

``` java
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    private Integer id;
    private String name;

    @ManyToOne // An Employee has one Department
    @JoinColumn(name = "department_id") // <<< This tells JPA which column in the 'employees' table holds the foreign key
    private Department department;

    // getters, setters...
}
```

- The `Employee` entity has a `@ManyToOne` relationship with `Department`.
- Critically, it uses `@JoinColumn(name = "department_id")`. This explicitly tells JPA: "When you need to figure out
  which department this employee belongs to in the database, look at the `department_id` column in the `employees`
  table. Manage this column when I set the `department` field."
- Because `Employee` maps the actual foreign key column (`department_id`), `Employee` **is the "owning side" of this
  relationship.**

**2. The `Department` Entity (Inverse/Non-Owning Side):**

``` java
@Entity
@Table(name = "departments")
public class Department {
    @Id
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "department") // <<< Tells JPA: "I don't manage the foreign key. Look at the 'department' field in the Employee entity to find the details."
    private List<Employee> employees;

    // getters, setters...
}
```

- The `Department` entity has a `@OneToMany` relationship with `Employee`.
- It uses `mappedBy = "department"`. This tells JPA: "I _don't_ have the foreign key column in my `departments` table.
  The relationship is actually managed by the `department` field over in the entity. Don't try to create a join table or
  manage foreign keys based on _this_ side." `Employee`
- Because `Department` does _not_ map the foreign key column and instead points back (`mappedBy`) to the field on the
  owning side,
  **`Department` is the "inverse side" or "non-owning side"** of the relationship.

**Why does it matter?**
JPA only considers the **owning side** when determining how to update the foreign key relationship in the database.

- If you do `employee.setDepartment(someDepartment);`, JPA knows it needs to update the `department_id` column in that
  employee's row in the `employees` table.
- If you _only_ do `department.getEmployees().add(someEmployee);` without also calling
  `someEmployee.setDepartment(department);`, JPA **will not** automatically update the `department_id` column for
  `someEmployee` in the database, because the `Department` side (with `mappedBy`) doesn't own/manage the foreign key
  column.

**In short:**
The **owning side** is the entity that contains the mapping definition (`@JoinColumn` or `@JoinTable`) for the physical
foreign key in the database. The other side (**inverse/non-owning side**) uses `mappedBy` to point back to the field on
the owning side.

### 1.2 For many-to-many relationships

Let's extend the concept to Many-to-Many relationships using a classic example: **Students** and **Courses**.

- A student can enroll in _many_ courses.
- A course can have _many_ students enrolled.

In a relational database, this requires a third table, often called a **join table** or **link table**:

1. **`students`** table:
    - `id` (Primary Key)
    - `name`

2. **`courses`** table:
    - `id` (Primary Key)
    - `title`

3. **`student_courses`** (Join Table):
    - `student_id` (Foreign Key referencing `students.id`)
    - `course_id` (Foreign Key referencing `courses.id`)
    - (Usually, `student_id` and `course_id` together form a composite primary key)

This `student_courses` table _is_ the physical representation of the relationship. It doesn't belong solely to
`students` or `courses`; it links them.
Now, in JPA, when you model this with `@ManyToMany`, you still need to tell JPA _which entity's mapping configuration_
it should use to manage this join table. **One side must be designated as the owning side.**
**1. The `Student` Entity (Designated as Owning Side):**

``` java
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }) // Example cascades
    @JoinTable(
            name = "student_courses", // <<< Name of the join table
            joinColumns = @JoinColumn(name = "student_id"), // <<< FK column in join table for THIS entity (Student)
            inverseJoinColumns = @JoinColumn(name = "course_id") // <<< FK column in join table for the OTHER entity (Course)
    )
    private Set<Course> courses = new HashSet<>(); // Collection of related courses

    // getters, setters, convenience methods for adding/removing courses...
    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudentsInternal().add(this); // Keep both sides in sync (helper needed on Course)
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.getStudentsInternal().remove(this); // Keep both sides in sync
    }
}
```

- We use `@ManyToMany`.
- Crucially, we use `@JoinTable`. This annotation explicitly defines:
    - The name of the join table (`student_courses`).
    - The foreign key column in the join table that links back to _this_ entity (`student_id` via `joinColumns`).
    - The foreign key column in the join table that links to the _other_ entity (`course_id` via `inverseJoinColumns`).

- Because `Student` defines the `@JoinTable`, **`Student` is the "owning side"** of this Many-to-Many relationship.

**2. The `Course` Entity (Inverse/Non-Owning Side):**

``` java
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany(mappedBy = "courses") // <<< Points to the 'courses' field in the Student entity
    private Set<Student> students = new HashSet<>(); // Collection of related students

    // getters, setters...

    // Package-private or protected helper for bidirectional management
    Set<Student> getStudentsInternal() {
        return students;
    }
}
```

- We use `@ManyToMany`.
- We use `mappedBy = "courses"`. This tells JPA: "I am _not_ responsible for managing the join table. The details of the
  relationship (the `@JoinTable` definition) are found on the `courses` field within the `Student` entity."
- Because `Course` uses `mappedBy` and does _not_ define the `@JoinTable`, **`Course` is the "inverse side" or "
  non-owning side"**.

**Why does it matter (again)?**
JPA only considers the configuration on the **owning side** (`Student` in this case) when deciding how to modify the *
*`student_courses` join table**.

- If you execute `student.getCourses().add(course);` and persist the `student`, JPA will look at the `@JoinTable`
  definition on `Student` and **insert a new row** into the `student_courses` table with the appropriate `student_id`
  and `course_id`.
- If you execute `student.getCourses().remove(course);` and persist the `student`, JPA will look at the `@JoinTable`
  definition and **delete the corresponding row** from `student_courses`.
- If you _only_ execute `course.getStudents().add(student);` (modifying the _inverse_ side) and persist the `course`,
  JPA **will do nothing** to the `student_courses` table because the `Course` side isn't configured to manage it (
  `mappedBy` signals this).

**In summary for Many-to-Many:**

- The relationship is physically stored in a join table.
- One entity class must be designated the **owning side** by defining the `@JoinTable`.
- The other entity class is the **inverse side** and _must_ use `mappedBy` on its `@ManyToMany` annotation, pointing to
  the collection field on the owning side.
- Changes to the relationship (adding/removing links) are persisted to the join table _only_ when the collection on the
  **owning side** is modified. (It's still best practice to keep both sides synchronised _in memory_ using helper
  methods).

#### 1.2.1 Helper methods to keep both sides synchronised

Let's break down why keeping both sides of a relationship synchronized _in memory_ is important, even though only the
owning side dictates database changes.
**The Problem: Inconsistent Object State**
Imagine our `Student` (owning side) and `Course` (inverse side) ManyToMany relationship.
If you only modify the owning side in your code like this:

``` java
// Assume 'student1' and 'math101' are managed JPA entities
Student student1 = entityManager.find(Student.class, 1L);
Course math101 = entityManager.find(Course.class, 101L);

// Modify ONLY the owning side's collection
student1.getCourses().add(math101);

//entityManager.persist(student1); // or merge, or transaction commit
// ^^^ JPA WILL update the student_courses join table correctly here because Student is the owning side.
```

**What happens in memory _before_ persisting?**

- `student1.getCourses()` now contains `math101`. ✅ (Correct)
- `math101.getStudents()` does **NOT** contain `student1`. ❌ (**Inconsistent!**)

Your Java objects are now out of sync with each other. The `student1` object "knows" it's enrolled in `math101`, but the
`math101` object doesn't "know" that `student1` is enrolled in it.
**Why is this In-Memory Inconsistency Bad?**

1. **Unexpected Behavior During the Same Transaction:** If, later _in the same transaction or unit of work_ (before the
   data is potentially reloaded from the DB), some other piece of code accesses `math101.getStudents()`, it won't see
   `student1`. This can lead to bugs if that code relies on having the complete, up-to-date list of students for that
   course _right now_.
2. **Broken Object Graph Navigation:** If you pass `math101` to a method that expects to iterate over all its students,
   it will miss `student1`. The connection is only visible from one direction in your current object graph.
3. **Testing Difficulties:** Unit tests or integration tests that assert the state of both sides of the relationship
   immediately after modification will fail if you don't synchronize.
4. **Code Clarity and Maintenance:** Explicitly managing both sides makes the relationship handling clearer and less
   prone to errors if someone refactors the code later without understanding the JPA owning/inverse side nuances.

**The Solution: Helper Methods for Synchronization**
This is where helper methods come in. You add methods to manage the relationship, and these methods update _both_ sides
of the relationship _in memory_ simultaneously.
Let's refine the `Student` (owning) and `Course` (inverse) entities:
**Student (Owning Side)**

``` java
// ... other imports and annotations
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "students")
public class Student {
    // ... id, name ...

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    // --- Helper Methods ---
    public void addCourse(Course course) {
        if (course != null) {
            this.courses.add(course);               // 1. Add to owning side collection
            course.getStudentsInternal().add(this); // 2. Add to inverse side collection (using helper)
        }
    }

    public void removeCourse(Course course) {
        if (course != null) {
            this.courses.remove(course);             // 1. Remove from owning side collection
            course.getStudentsInternal().remove(this); // 2. Remove from inverse side collection (using helper)
        }
    }

    // Standard getter might still be useful
    public Set<Course> getCourses() {
        return courses; // Or return an unmodifiable view
    }
    // ... other getters/setters ...
}
```

**Course (Inverse Side)**

``` java
// ... other imports and annotations
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "courses")
public class Course {
    // ... id, title ...

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    // --- Helper for synchronization ---
    // Often package-private or protected to discourage direct modification from outside,
    // forcing use of the owning side's helper methods (Student.addCourse).
    Set<Student> getStudentsInternal() {
        return students;
    }

    // Public getter for read access
    public Set<Student> getStudents() {
        return students; // Or return an unmodifiable view
    }
    // ... other getters/setters ...
}
```

**How it Works Now:**

``` java
// Assume 'student1' and 'math101' are managed JPA entities
Student student1 = entityManager.find(Student.class, 1L);
Course math101 = entityManager.find(Course.class, 101L);

// Use the helper method on the owning side
student1.addCourse(math101);

// --- In-Memory State Check (BEFORE persisting) ---
// student1.getCourses() now contains math101.        ✅ Correct!
// math101.getStudents() ALSO now contains student1.   ✅ Correct and Consistent!

//entityManager.persist(student1); // or merge, or transaction commit
// ^^^ JPA still only looks at student1.getCourses() (the owning side)
//     to figure out it needs to insert into student_courses.
//     But our objects are already consistent beforehand.
```

By using `student1.addCourse(math101)`, you ensure that both the `student1` object _and_ the `math101` object correctly
reflect the new relationship _in memory_, immediately. This leads to a more predictable and robust object model, even
though the database persistence logic still hinges solely on the owning side's state.

