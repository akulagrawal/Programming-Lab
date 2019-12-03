{- 	A:
	Define a function m that takes a lists of lists of integers and sums
	the numbers in each of the innermost lists together before
	multiplying the resulting sums with each other. For example,
	*Main> m [[1,2],[3,5]]
	24
	*Main> m [[1,2,3],[7]]
	42
	*Main> m [[7,8],[]]
	0
	*Main> m [[1,2],[2,3],[4,5,6]]
	225
-}

sum1 :: [Int] -> Int
sum1 []     = 0
sum1 (x:xs) = x + sum1 xs

prod1 :: [Int] -> Int
prod1 []     = 1
prod1 (x:xs) = x * prod1 xs

sum2 :: [[Int]] -> [Int]
sum2 [] = []
sum2 (x:xs) = sum1 x : sum2 xs

m :: [[Int]] -> Int
m (x:xs) = prod1 (sum2 (x:xs))


{- 	B:
	Write a function called greatest, which has the following signature:
	greatest :: (a -> Int) -> [a] -> a
	greatest f seq returns the item in seq that maximizes function f.
	For example:
	*Main> greatest sum [[2,5], [-1,3,4], [2]]
	[2,5]
	*Main> greatest length ["the", "quick", "brown", "fox"]
	"quick"
	*Main> greatest id [51,32,3]
	51
	NB: If more than one item maximizes f, then greatest f returns the first one.
-}
greatest :: (a -> Int) -> [a] -> a
greatest f x
        |length x == 1          = first
        |f first >= f second    = first
        |otherwise              = second
        where first   = head x
              second  = greatest f (tail x)

{-  C:
	Implement toList :: [a] -> List a , which converts a regular Haskell list
	to a List a ; and vice versa. For example,
	*Main> toList []
	Empty
	*Main> toList [2, 7, 4]
	Cons 2 (Cons 7 (Cons 4 Empty))
	*Main> toList "apple"
	Cons 'a' (Cons 'p' (Cons 'p' (Cons 'l' (Cons 'e' Empty))))
	AND
	*Main> toHaskellList Empty
	[]
	*Main> toHaskellList (Cons 2 (Cons 7 (Cons 4 Empty)))
	[2,7,4]
	*Main> toHaskellList (Cons "cat" (Cons "bat" (Cons "rat" Empty)))
	["cat","bat","rat"]
	Deriving Show defines the function show, which converts a value into a string
-}
data List a = Empty | Cons a (List a)
  deriving Show

toList :: [a] -> List a
toList []     = Empty
toList (x:xs) = Cons x (toList xs)

toHaskellList :: List a -> [a]
toHaskellList Empty        = []
toHaskellList (Cons x xs)  = x : toHaskellList xs