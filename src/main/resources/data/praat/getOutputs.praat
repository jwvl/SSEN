# Function to test OTMulti and PairDistributions
noise = 2.0
triesPerItem = 100
otMulti = selected("OTMulti")
itemsInTop = 3
pd = selected("PairDistribution")
select pd
nPairs = do("Get number of pairs")


for i to nPairs
   leftString$ = do$("Get string1...", i)
   select otMulti
   output = do ("To output Distribution...", leftString$, "", triesPerItem, noise)
   tempTable = do("To Table...", "Input")
   do ("Set column label (index)...",2,"Frequency")
   table[i] = do ("Extract rows where column (number)...", "Frequency", "greater than", 0)


   do ("Sort rows...", "Frequency")
   nRows =do("Get number of rows")
   if nRows > itemsInTop
      for j from (nRows-itemsInTop) to 1
         do("Remove row...",j)
     endfor
   endif
   select output
   plus tempTable
   Remove
   select pd
endfor

select table[1]
for i from 2 to nPairs
   plus table[i]
endfor
grandTable = do("Append")
#noZeroes = do ("Extract rows where column (number)...", "Frequency", "greater than", 10)
#do ("Sort rows...", "Frequency")

for i from 1 to nPairs
   select table[i]
   Remove
endfor
