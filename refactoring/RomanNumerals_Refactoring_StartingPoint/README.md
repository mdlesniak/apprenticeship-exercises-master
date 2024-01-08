 Instructions:  Please refactor this until it passes the checkstyle rules.  Pay particular
                attention to separation of concerns - is there another class (or two, or
                three) hiding in there?
                
                Add a validator to ensure that the number is something that you can convert.
                Use the rule that you can't repeat any Roman digit more than three times.
                
                Add support for the reverse process - converting a Roman numeral to it's
                decimal value.
                
                You should apply the following rules for all conversions:
 			    - A number written in Arabic numerals can be broken into digits. 
                  For example, 1903 is composed of 1 (one thousand), 9 (nine hundreds), 
                  0 (zero tens), and 3 (three units). To write the Roman numeral, 
                  each of the non-zero digits should be treated separately. In the 
                  above example, 1,000 = M, 900 = CM, and 3 = III. Therefore,
                  1903 = MCMIII.
                - The symbols "I", "X", "C", and "M" can be repeated three times in 
                  succession, but no more. (They may appear more than three times if 
                  they appear non-sequentially, such as XXXIX.) "D", "L", and "V" can 
                  never be repeated.
                - "I" can be subtrac                ted from "V" and "X" only. "X" can be subtracted 
                  from "L" and "C" only. "C" can be subtracted from "D" and "M" only. 
                  "V", "L", and "D" can never be subtracted Only one small-value 
                  symbol may be subtracted from any large-value symbol. */
