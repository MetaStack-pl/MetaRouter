package trail

import org.scalatest.freespec.AnyFreeSpec

class RouteEqualityTests extends AnyFreeSpec {
  "A Route" - {
    "cannot equal InstantiatedRoute" in {
      val r1 = Root / "asdf"
      val r2 = r1(())
      assert(!r1.canEqual(r2), "r1 should not be comparable to r2")
    }
    "cannot equal a non-route" in {
      val r1 = Root / "asdf"
      assert(!r1.canEqual(2), "r1 should not be comparable to an integer")
      assert(!r1.canEqual("Asdf"), "r1 should not be comparable to a string")
      assert(r1 !== 2)
    }
    "when empty" - {
      "should compile" in {
        val root = Root
      }
      "should equal the empty route" in {
        val root = Root
        val root2 = Root
        assert(root === root2)
      }
    }
    "when there are no Args" - {
      "should compile" in {
        val r = Root / "asdf"
      }
//      "apply() with arguments should not compile" in {
//        """
//          val r = Root / "asdf"
//          r(1)
//        """ shouldNot typeCheck
//      }
      "can compute its hashcode consistently" in {
        val r1 = Root / "asdf"
        val r2 = Root / "asdf"
        assert(r1.hashCode() === r1.hashCode())
        assert(r1.hashCode() === r2.hashCode())
      }
      "should equal an identical route" in {
        val foo = !# / "asdf" / "fdas"
        val bar = !# / "asdf" / "fdas"

        assert(foo === bar)
      }
      "should not equal a similiar route" in {
        val foo = !# / "asdf" / "fdas"
        val bar = !# / "asdf" / "fdas1"

        assert(foo !== bar)
      }
      "should not equal a longer route" in {
        val foo = !# / "asdf" / 1
        val bar = !# / "asdf" / 1 / "fdas"

        assert(foo !== bar)
      }
      "should not equal a longer shorter route" in {
        val bar = !# / "asdf" / 1 / "fdas"
        val foo = !# / "asdf" / 1

        assert(bar !== foo)
      }
    }
    "when there is one Arg" - {
      "should compile" in {
        val route = Root / "String" / Arg[Int]
      }
      "can compute its hashcode consistently" in {
        val r1 = Root / "String" / Arg[Int]
        val r2 = Root / "String" / Arg[Int]
        assert(r1.hashCode() === r1.hashCode())
        assert(r1.hashCode() === r2.hashCode())
      }
      "should compute the same hashcode as a similar route with different arg names" in {
        val r1 = Root / "String" / Arg[Int]
        val r2 = Root / "String" / Arg[Int]
        assert(r1.hashCode() === r1.hashCode())
        assert(r1.hashCode() === r2.hashCode())
      }
      "should equal an identical route" in {
        val foo = !# / "asdf" / Arg[Int]
        val bar = !# / "asdf" / Arg[Int]

        assert(foo === bar)
      }
      "should equal a similar route with different arg names" in {
        val foo = !# / "asdf" / Arg[Int]
        val bar = !# / "asdf" / Arg[Int]

        assert(foo === bar)
      }
      "should not equal another route with different arg names and types" in {
        val foo = !# / "asdf" / Arg[Int]
        val bar = !# / "asdf" / Arg[Boolean]

        assert(foo !== bar)
      }
      "should not equal another route with different types but same names" in {
        val foo = !# / "asdf" / Arg[Int]
        val bar = !# / "asdf" / Arg[Boolean]

        assert(foo !== bar)
      }
    }
    "when there are multiple Args" - {
      "should compile" in {
        val r = Root / Arg[String] / "asdf" / Arg[Int]
      }
      "should equal an identical route" in {
        val foo = !# / "asdf" / Arg[Int] / Arg[Boolean]
        val bar = !# / "asdf" / Arg[Int] / Arg[Boolean]

        assert(foo === bar)
      }
      "can compute its hashcode consistently" in {
        val r1 = !# / "asdf" / Arg[Int] / Arg[Boolean]
        val r2 = !# / "asdf" / Arg[Int] / Arg[Boolean]
        assert(r1.hashCode() === r1.hashCode())
        assert(r1.hashCode() === r2.hashCode())
      }
      "should compute the same hashcode as a similar route with different arg names" in {
        val foo = !# / "asdf" / Arg[Int] / Arg[Boolean]
        val bar = !# / "asdf" / Arg[Int] / Arg[Boolean]
        assert(foo.hashCode() === foo.hashCode())
        assert(foo.hashCode() === bar.hashCode())
      }
      "should equal a similar route with different arg names" in {
        val foo = !# / "asdf" / Arg[Int] / Arg[Boolean]
        val bar = !# / "asdf" / Arg[Int] / Arg[Boolean]

        assert(foo === bar)
      }
      "should not equal another route with different arg names and types" in {
        val foo = !# / "asdf" / Arg[Int] / Arg[Boolean]
        val bar = !# / "asdf" / Arg[Boolean] / Arg[Int]

        assert(foo !== bar)
        assert(foo.hashCode() !== bar.hashCode())
      }
      "should not equal another route with different types but same names" in {
        val foo = !# / "asdf" / Arg[Int] / Arg[Boolean]
        val bar = !# / "asdf" / Arg[Boolean] / Arg[Int]

        assert(foo !== bar)
        assert(foo.hashCode() !== bar.hashCode())
      }
    }
    "when using a custom path element" - {
      case class Foo(foo: String)
      implicit object FooElement extends StaticElement[Foo](_.foo)
      "should compile" in {
        val r = Root / Foo("asdf")
      }
    }
    "when using a custom Arg element" - {
      case class Foo(bar: String)
      implicit object FooCodec extends Codec[Foo] {
        override def encode(s: Foo): Option[String] = Some(s.bar)
        override def decode(s: Option[String]): Option[Foo] = s.map(Foo)
      }
      "should compile" in {
        val r = Root / Arg[Foo]
      }
    }
  }
}