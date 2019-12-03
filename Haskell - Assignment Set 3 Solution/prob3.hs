import System.Random
import Control.Monad
import Data.List
import Data.Function
import System.IO



randomize xs = do
  ys <- replicateM (length xs)  (randomRIO (1 :: Int, 100000))
  pure $ map fst ( sortBy (compare `on` snd) (zip xs ys))

draw = do
  let x = ["BS","CM","CH","CV","CS","DS","EE","HU","MA","ME","PH","ST"]
  let date = ["1-11", "1-11", "1-11", "1-11", "2-11", "2-11", "2-11", "2-11", "3-11", "3-11", "3-11", "3-11"]
  let time = ["9:30", "9:30", "7:30", "7:30", "9:30", "9:30", "7:30", "7:30", "9:30", "9:30", "7:30", "7:30"]
  writeFile "date.txt" (unlines date)
  writeFile "time.txt" (unlines time)
  y <- randomize x
  writeFile "file.txt" (unlines y)
  putStr . id $ ""



printall idx = do
  if idx < 12 then do
    datef <- readFile ("date.txt")
    timef <- readFile ("time.txt")
    xf <- readFile ("file.txt")
    let date = lines datef
    let time = lines timef
    let x = lines xf
    putStr . id $ (x!!idx)
    putStr . id $ " vs "
    putStr . id $ (x!!(idx+1))
    putStr . id $ " "
    putStr . id $ (date!!idx)
    putStr . id $ " "
    putStrLn . id $ (time!!idx)
    printall (idx+2)
  else putStr . id $ ""



printone idx y = do
  if idx < 12 then do
    datef <- readFile ("date.txt")
    timef <- readFile ("time.txt")
    xf <- readFile ("file.txt")
    let date = lines datef
    let time = lines timef
    let x = lines xf
    if (((isSubsequenceOf (y!!0) (x!!idx)) && (isSubsequenceOf (x!!idx) (y!!0))) || ((isSubsequenceOf (y!!0) (x!!(idx+1))) && (isSubsequenceOf (x!!(idx+1)) (y!!0)))) then do
      putStr . id $ (x!!idx)
      putStr . id $ " vs "
      putStr . id $ (x!!(idx+1))
      putStr . id $ " "
      putStr . id $ (date!!idx)
      putStr . id $ " "
      putStrLn . id $ (time!!idx)
    else printone (idx+2) y
  else putStrLn . id $ "WRONG INPUT"



printattime idx = do
  if idx < 12 then do
    datef <- readFile ("date.txt")
    timef <- readFile ("time.txt")
    xf <- readFile ("file.txt")
    df <- readFile (".vardate.txt")
    tf <- readFile (".vartime.txt")
    let date = lines datef
    let time = lines timef
    let x = lines xf
    let d = lines df
    let t = lines tf
    if ((isSubsequenceOf (d!!0) (date!!idx)) && (isSubsequenceOf (date!!idx) (d!!0)) && (isSubsequenceOf (t!!0) (time!!idx)) && (isSubsequenceOf (time!!idx) (t!!0))) then do
      putStr . id $ (x!!idx)
      putStr . id $ " vs "
      putStr . id $ (x!!(idx+1))
      putStr . id $ " "
      putStr . id $ (date!!idx)
      putStr . id $ " "
      putStrLn . id $ (time!!idx)
    else putStr . id $ ""
    printattime (idx+2)
  else putStr . id $ ""



nextMatch x y = do
  if ((x<1)||(x>30)) then do
    putStrLn . id $ "INVALID DATE"
  else do
    if (x==1) then do
      if (y>=24) then do
        putStrLn . id $ "INVALID TIME"
      else do
        if (y<9.5) then do
          let dz = ["1-11"]
          let tz = ["9:30"]
          writeFile ".vardate.txt" (unlines dz)
          writeFile ".vartime.txt" (unlines tz)
        else do
          if ((y>=9.5) && (y<19.5)) then do
            let dz = ["1-11"]
            let tz = ["7:30"]
            writeFile ".vardate.txt" (unlines dz)
            writeFile ".vartime.txt" (unlines tz)
          else do
            let dz = ["2-11"]
            let tz = ["9:30"]
            writeFile ".vardate.txt" (unlines dz)
            writeFile ".vartime.txt" (unlines tz)
        printattime 0
    else do
      if (x==2) then do
        if (y>=24) then do
          putStrLn . id $ "INVALID TIME"
        else do
          if (y<9.5) then do
            let dz = ["2-11"]
            let tz = ["9:30"]
            writeFile ".vardate.txt" (unlines dz)
            writeFile ".vartime.txt" (unlines tz)
          else do
            if ((y>=9.5) && (y<19.5)) then do
              let dz = ["2-11"]
              let tz = ["7:30"]
              writeFile ".vardate.txt" (unlines dz)
              writeFile ".vartime.txt" (unlines tz)
            else do
              let dz = ["3-11"]
              let tz = ["9:30"]
              writeFile ".vardate.txt" (unlines dz)
              writeFile ".vartime.txt" (unlines tz)
          printattime 0
      else do
        if (x==3) then do
          if (y>=24) then do
            putStrLn . id $ "INVALID TIME"
          else do
            if (y<9.5) then do
              let dz = ["3-11"]
              let tz = ["9:30"]
              writeFile ".vardate.txt" (unlines dz)
              writeFile ".vartime.txt" (unlines tz)
              printattime 0
            else do
              if ((y>=9.5) && (y<19.5)) then do
                let dz = ["3-11"]
                let tz = ["7:30"]
                writeFile ".vardate.txt" (unlines dz)
                writeFile ".vartime.txt" (unlines tz)
                printattime 0
              else do
                putStrLn . id $ "ALL MATCHES OVER"
        else do
          putStrLn . id $ "ALL MATCHES OVER"



fixture x = do
  if (x=="all") then do
    printall 0
  else do
    let xf = [x]
    printone 0 xf