module Anagram where
import Data.List
import Test.QuickCheck

isAnagram x y = sort x == sort y && length x == length y

{-
*Main> printAnagram "xy" ["xyz","yx"]
1
-}
findAnagramx t [] = 0
findAnagramx t (x:xs) = do if isAnagram t x
                              then do  (1 + findAnagramx t xs)
                              else findAnagramx t xs

{-
*Main> printElements ["xxyy","xxy","xx","x","xyy","xy","x","yy","y","y"]
2
-}
findAllAnagrams [] = 0
findAllAnagrams (x:xs) = do
                           let a = findAnagramx x xs
                           let b = findAllAnagrams xs
                           a+b

{-
*Main> tails "xyz"
["xyz","yz","z",""]
*Main> inits "xyz"
["","x","xy","xyz"]
*Main> (inits . tails) "xyz"
[[],["xyz"],["xyz","yz"],["xyz","yz","z"],["xyz","yz","z",""]]
*Main> concatMap (inits . tails) ["xyz"]
[[],["xyz"],["xyz","yz"],["xyz","yz","z"],["xyz","yz","z",""]]
*Main> concatMap (inits . tails) ["x","y","z"]
[[],["x"],["x",""],[],["y"],["y",""],[],["z"],["z",""]]
*Main> concat [['x','y'], ['x', 'y']]
"xyxy"
-}
count x = do let a = concat x
             let b = concatMap (tail . inits) (tails a)
             findAllAnagrams b
