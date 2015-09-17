% Learn you a Haskell for great Scala!

# Motivation

What concepts from Haskell can help us when writing Scala?

. . .

* Preliminaries
    * functions, type signatures
    * datatypes
* Today's focus: thinking algebraically about programs
    * **abstraction:** abstracting over algebraic concepts using typeclasses
    * **"programs for free":** typeclass instance resolution
    * **higher-order abstraction:** higher-kinded types

# Functions

```{.haskell}
name :: String
name = "Austin Scala"

append :: String -> String -> String   -- signature on its own line
append x y = x ++ y
```

```{.haskell}
ghci> append "Hello, " name
"Hello, Austin Scala"

ghci> :t append "Hello, "              -- functions are "curried"
append "Hello, " :: String -> String
```


# Datatypes

```{.haskell}
data Bool       = False | True
data Maybe a    = Nothing | Just a
data Either a b = Left a | Right b
data MyPair a b = Pair a b
data MyList a   = Nil | Cons a (MyList a)
```

```{.haskell}
-- Haskell already has its own pairs and lists, with special syntactic sugar

(3,4)            -- Pair 3 4

1 : 2 : 3 : []   -- Cons 1 (Cons 2 (Cons 3 Nil))
[1,2,3]          -- same as above
```

# Datatypes: pattern matching

```{.haskell}
maybe :: b -> (a -> b) -> Maybe a -> b    -- "Expression style"
maybe b f ma =
  case ma of
    Nothing -> b
    Just a  -> f a

maybe' :: b -> (a -> b) -> Maybe a -> b   -- "Declaration style"
maybe' b _ Nothing  = b
maybe' _ f (Just a) = f a
```

```{.haskell}
ghci> maybe "N/A" show (Just 32)          -- 'show' is like toString
"32"
ghci> maybe "N/A" show Nothing
"N/A"
```

# Abstraction

We often run into code with commonalities:
```{.haskell}
concat :: [String] -> String
concat []     = ""
concat (x:xs) = x ++ concat xs

sum :: [Int] -> Int
sum []     = 0
sum (x:xs) = x + sum xs
```
```{.haskell}
-- The common pattern, in pseudo-code:
combineThings :: [thing] -> thing
combineThings []     = someThing
combineThings (x:xs) = x `thingOperation` combineThings xs
```


# Abstraction: thinking algebraically

A **monoid** is

1. a set
2. an associative operation `<>`, and
3. a neutral element `e` in the set

satisfying the **monoid laws**:

```{.haskell}
forall x.              e <> x = x               -- left identity
forall x.              x <> e = x               -- right identity
forall x y z.   (x <> y) <> z = x <> (y <> z)   -- associativity
```


# Abstraction: thinking algebraically

* Examples of monoids
    * Integers with addition and zero
    * Strings with concatenation and the empty string
    * Maps with map union and the empty map
    * [https://izbicki.me/blog/gausian-distributions-are-monoids](https://izbicki.me/blog/gausian-distributions-are-monoids)


# Abstraction: thinking algebraically

* Examples of monoids
    * [Diagrams with **`atop`**](http://projects.haskell.org/diagrams/doc/quickstart.html#combining-diagrams) and the empty picture

```{.haskell}
example = square 1 # fc aqua `atop` circle 1
```

![](atop.png)


# Abstraction with `class`

```{.haskell}
class Monoid a where       -- typeclass
  mempty :: a
  (<>)   :: a -> a -> a

  -- laws:
  --   left identity    mempty <> x   = x
  --   right identity   x <> mempty   = x
  --   associativity    x <> (y <> z) = (x <> y) <> z
```

. . .

```{.haskell}
instance Monoid Int where  -- typeclass instances
  mempty = 0
  x <> y = x + y

instance Monoid String where
  mempty = ""
  (<>)   = (++)            -- aside: pointfree style
```

# Abstraction with `class`

```{.haskell}
mconcat :: Monoid a => [a] -> a      -- one definition, multiple realizations
mconcat []     = mempty
mconcat (x:xs) = x <> mconcat xs
```
```{.haskell}
concat :: [String] -> String         -- concat :: [String] -> String
concat []     = ""                   -- concat = mconcat
concat (x:xs) = x ++ concat xs

sum :: [Int] -> Int                  -- sum :: [Int] -> Int
sum []     = 0                       -- sum = mconcat
sum (x:xs) = x + sum xs
```
```{.haskell}
ghci> mconcat [1,2,3]
6
ghci> mconcat ["a","b","c"]
"abc"
```

# "Programs for free": instance resolution

```{.haskell}
instance (Monoid a, Monoid b) => Monoid (a,b) where
  mempty         = (mempty, mempty)
  (a,b) <> (c,d) = (a <> c, b <> d)
```

```{.haskell}
ghci> -- the compiler figures out the correct mempty and (<>) to use
ghci> mempty :: Int
0
ghci> mempty :: String
""
ghci> mempty :: (String, Int)
("",0)
ghci> mempty :: (Int, (Int, String))
(0,(0,""))
ghci> mconcat [(1,(2,"a")), (7,(2,"b")), (0,(3,"c"))] :: (Int, (Int, String))
(8,(7,"abc"))
```

# Higher-order abstraction

```{.haskell}
mapMaybe :: (a -> b) -> Maybe a -> Maybe b
mapMaybe f Nothing  = Nothing
mapMaybe f (Just a) = Just (f a)

mapEither :: (a -> b) -> Either e a -> Either e b
mapEither f (Right a) = Right (f a)
mapEither f (Left e)  = Left e            -- If it helps, think of:

mapPair :: (a -> b) -> (c, a) -> (c, b)   -- ... -> MyPair c a -> MyPair c b
mapPair f (c, a) = (c, f a)

mapList :: (a -> b) -> [a] -> [b]         -- ... -> MyList a -> MyList b
mapList f []     = []
mapList f (a:as) = f a : mapList f as
```
. . .
```{.haskell}
-- The common pattern, in pseudo-code:
mapThing :: (a -> b) -> (thing a) -> (thing b)
```

# Kinds: the "types" of types

Terms have types:

```{.haskell}
ghci> :type True
True :: Bool
ghci> :type append
append :: String -> String -> String
ghci> :type append "Hello"
append "Hello" :: String -> String
```

Types have kinds:

```{.haskell}
ghci> :kind Bool
Bool :: *
ghci> :kind Either
Either :: * -> * -> *
ghci> :kind Either Int
Either Int :: * -> *
```

# The `Functor` class

```{.haskell}
class Functor f where               -- f must have kind (* -> *)
  fmap :: (a -> b) -> f a -> f b

  -- laws:
  --   identity                fmap id = id
  --   associativity  fmap f . fmap g  = fmap (f . g)
```

. . .

```{.haskell}
instance Functor Maybe where
  fmap = mapMaybe

instance Functor Either where       -- Kind error! This won't compile
  fmap = mapEither

instance Functor (Either e) where   -- This compiles
  fmap = mapEither
```

# The `Functor` class

```{.haskell}
increment :: Functor f => f Int -> f Int
increment = fmap (+1)
```

```{.haskell}
ghci> increment [1,2,3]
[2,3,4]
ghci> increment (Just 7)
Just 8
ghci> increment (Right 10)
Right 11
ghci> increment ("hello", 0)
("hello",1)
```

# What is "higher-kinded"?


Higher-order function:
```{.haskell}
ghci> -- takes a function ("parameterized value") as an argument
ghci> :t map
map :: (a -> b) -> [a] -> [b]
```

Higher-kinded type:
```{.haskell}
ghci> -- takes a parameterized type as an argument
ghci> :k Functor
(* -> *) -> Constraint
```
