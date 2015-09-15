% Learn you a Haskell for great Scala!

# Motivation

What concepts from Haskell can help us when writing Scala?

. . .

* Preliminaries
    * functions, type signatures
    * datatypes
* Today's focus:
    * **abstraction:** abstracting over algebraic concepts using typeclasses
    * **more predictable refactoring:** typeclass laws
    * **"programs for free":** instance resolution
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
ghci> maybe "N/A" show (Just 32)
"32"
ghci> maybe "N/A" show Nothing
"N/A"
```

# Thinking algebraically

*Example:* associative operations with a neutral element

```{.haskell}
("hello" ++ "world") ++ "" == "hello" ++ ("world" ++ "")

(3 + 5) + 0 == 3 + (5 + 0)
```
. . .

```{.haskell}
concat :: [String] -> String
concat []     = ""
concat (x:xs) = x ++ concat xs

sum :: [Int] -> Int
sum []     = 0
sum (x:xs) = x + sum xs
```

We'd like to abstract over this pattern.


# Thinking algebraically with typeclasses

```{.haskell}
class Monoid a where      -- not in the OOP sense!
  mempty :: a
  (<>)   :: a -> a -> a

  -- laws:
  --   left identity    mempty <> x == x
  --   right identity   x <> mempty == x
  --   associativity    x <> (y <> z) == (x <> y) <> z
```

. . .

```{.haskell}
instance Monoid Int where
  mempty = 0
  x <> y = x + y

instance Monoid String where
  mempty = ""
  (<>)   = (++)
```

# Monoid-generic functions

```{.haskell}
concat :: [String] -> String         -- concat :: [String] -> String
concat []     = ""                   -- concat = mconcat
concat (x:xs) = x ++ concat xs

sum :: [Int] -> Int                  -- sum :: [Int] -> Int
sum []     = 0                       -- sum = mconcat
sum (x:xs) = x + sum xs

mconcat :: Monoid a => [a] -> a
mconcat []     = mempty
mconcat (x:xs) = x <> mconcat xs
```
```{.haskell}
ghci> mconcat [1,2,3]
6
ghci> mconcat ["a","b","c"]
"abc"
```

# Composing typeclass instances

```{.haskell}
instance (Monoid a, Monoid b) => Monoid (a,b) where
  mempty         = (mempty, mempty)
  (a,b) <> (c,d) = (a <> c, b <> d)


mempty :: Int
mempty :: String
mempty :: (Int, String)

-- compiler figures out the correct mempty and (<>) to use
mconcat [(1,(2,"a")), (7,(2,"b")), (0,(3,"c"))]
```

# Mapping

mapMaybe
mapList

abstracting over * -> *
