{-# LANGUAGE GADTs #-}
{-# LANGUAGE DeriveFunctor #-}
{-# LANGUAGE RankNTypes #-}
module Main where


import Data.Function(fix)


type Algebra f a = f a -> a
type Coalgebra f a = a -> f a

newtype Fix f = Fix { unFix :: f (Fix f) }
newtype Mu f = Mu { runMu :: forall r. Algebra f r -> r}
data Nu f = forall x. Nu x (Coalgebra f x)


class FixLike g where
    inF :: Functor f => f (g f) -> g f
    outF :: Functor f => g f -> f (g f)

instance FixLike Fix where
    inF = Fix
    outF = unFix

instance FixLike Mu where
    inF fMuF = Mu (\k -> k $ (flip runMu) k <$> fMuF)
    outF muF = runMu muF $ fmap inF

instance FixLike Nu where
    inF fNuF = Nu fNuF $ fmap outF
    outF (Nu x psi) = (flip Nu) psi <$> psi x


muToNu :: Functor f => Mu f -> Nu f
muToNu (Mu k) = k $ \fnu -> Nu fnu (fmap (\(Nu x fx) -> fmap (`Nu` fx) (fx x)))

nuToMu :: Functor f => Nu f -> Mu f
nuToMu (Nu x fx) = Mu $ \k -> let f = k . fmap f . fx in f x

muToFix :: Mu f -> Fix f
muToFix (Mu k) = k Fix

fixToMu :: Functor f => Fix f -> Mu f
fixToMu (Fix x) = Mu $ \k -> let f = k . fmap (f . unFix) in f x

nuToFix :: Functor f => Nu f -> Fix f
nuToFix (Nu x fx) = Fix $ fmap (nuToFix . (`Nu` fx)) (fx x)

fixToNu :: Fix f -> Nu f
fixToNu x = Nu x unFix


banana :: (FixLike g, Functor f) => Algebra f x -> g f -> x
banana phi = fix (\f -> phi . fmap f . outF)

lens :: (FixLike g, Functor f) => Coalgebra f x -> x -> g f
lens psi = fix (\f -> inF . fmap f . psi)

envelope :: Functor f => Coalgebra f x -> Algebra f x -> x -> x
envelope psi phi = fix (\f -> phi . fmap f . psi)


data NatF x = Zero | Succ x deriving Functor
type Nat = Mu NatF

zero :: Nat
zero = inF Zero

succNat :: Nat -> Nat
succNat nat = inF $ Succ nat

natToNum :: Num a => Nat -> a
natToNum = banana phi
    where
    phi Zero = 0
    phi (Succ x) = 1 + x


data StackF a x = Empty | Push a x deriving Functor
type Stack a = Mu (StackF a)

emptyStack :: Stack a
emptyStack = inF Empty

push :: a -> Stack a -> Stack a
push a s = inF $ Push a s

stackToList :: Stack a -> [a]
stackToList = banana phi
    where
    phi Empty = []
    phi (Push a x) = a : x


data SignalF a x = SignalF a x deriving Functor
type Signal a = Nu (SignalF a)

signal :: a -> (a -> a) -> Signal a
signal a step = lens psi a
    where psi x = SignalF x (step x)

signalToList :: Signal a -> [a]
signalToList = banana phi
    where
    phi (SignalF a x) = a : x


factorial :: (Num a, Ord a) => a -> a
factorial = envelope psi phi
    where
    z = fromInteger 0
    one = fromInteger 1
    psi i = if i > z then Push i (i - one) else Empty
    phi Empty = one
    phi (Push x y) = x * y


main :: IO ()
main = do
    print . (natToNum :: Nat -> Int) . succNat . succNat . succNat $ zero
    print . stackToList . push 1 . push 2 . push (3::Int) $ emptyStack
    print . take 5 . signalToList $ signal (0::Int) (+1)
    print $ factorial (5:: Int)
