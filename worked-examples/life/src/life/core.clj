(ns life.core
  (:use midje.sweet)
  (:require [clojure.set :as set]))

;;; Note that you need to read this file from the bottom to the top to
;;; follow the flow of development.

;;; STEP 5
;;; It is only here, in this last step, that I commit to the familiar
;;; Cartesian x,y coordinates.

(defn surroundings [[x y]]
  (set (for [xinc [-1 0 1]
             yinc [-1 0 1]
             :when (not= [xinc yinc] [0 0])]
         [(+ x xinc) (+ y yinc)])))
  

(fact "The universe is a flat grid, so each cell has eight cells in its surroundings"
  (surroundings [1 1]) => #{ [0 2] [1 2] [2 2]
                             [0 1]       [2 1]
                             [0 0] [1 0] [2 0]})

;;; STEP 4
;;; The two functions introduced in the previous step are easy to
;;; implement.  It was further clear that they were really the same
;;; function, but I decided it's still useful to make the distinction,
;;; since it's natural (?) when talking about the domain.

(defn living-cells-around [cell living-cells]
  (set/intersection (surroundings cell) living-cells))

(def immediate-neighbors living-cells-around)
(def potential-parents living-cells-around)

(fact "The immediate neighbors are the living cells in the surroundings"
  (immediate-neighbors ..cell.. #{..living..}) => #{..living..}
  (provided
    (surroundings ..cell..) => #{..living.. ..dead..}))

(fact "The potential parents are the living cells in the surroundings"
  (potential-parents ..cell.. #{..living..}) => #{..living..}
  (provided
    (surroundings ..cell..) => #{..living.. ..dead..}))
  


;;; STEP 3
;;; Births and survivors: the collection functions.

(defn births [fringe living-cells]
  (set (filter #(= 3 (count (potential-parents % living-cells))) fringe)))

(fact "Births depend on the number of potential parents a fringe cell has in the set of living cells"
  (births #{..too-few-parents.. ..too-many-parents.. ..just-right..} ..living..) => #{..just-right..}
  (provided
    (potential-parents ..too-few-parents.. ..living..) =>  #{.1. ..2..}
    (potential-parents ..just-right.. ..living..) =>       #{.1. ..2.. ..3..}
    (potential-parents ..too-many-parents.. ..living..) => #{.1. ..2.. ..3.. ..4..}))


(defn survivors [living-cells]
  (set (filter #(#{2 3} (count (immediate-neighbors % living-cells))) living-cells)))


(fact "A cell survives into the next generation if it has the right number of immediate neighbors"
  (let [living #{..too-crowded.. ..too-lonely.. ..on-the-low-side.. ..on-the-high-side..}]
    (survivors living) => #{..on-the-low-side.. ..on-the-high-side..}
    (provided
      (immediate-neighbors ..too-lonely.. living) =>       #{.1.}
      (immediate-neighbors ..on-the-low-side.. living) =>  #{.1. ..2..}
      (immediate-neighbors ..on-the-high-side.. living) => #{.1. ..2.. ..3..}
      (immediate-neighbors ..too-crowded.. living) =>      #{.1. ..2.. ..3.. ..4..})))

  
;;; STEP 2
;;; Forming the fringe from the living neighbors can be so naturally
;;; done with sets that I commit to a set representation for my
;;; containers. It seems like it'd be tedious to keep pretending
;;; otherwise. Of course, if I decide to make a more efficient
;;; representation, I'll have to rework tests, though not in any
;;; huge way. 
;;;
;;; Note that I still don't commit to how cells are represented nor to
;;; how many cells may be in a cell's surroundings. In fact, I needn't
;;; even commit to the fact that if living cell A is adjacent to living
;;; cell B, the reverse is also true.

(defn fringe [living]
  (let [all-possibilities (apply set/union (map surroundings living))]
    (set/difference all-possibilities living)))

(fact "the fringe are all non-living cells in the 'surroundings' of the living cells"
  ;; Let's illustrate some interesting cases
  (let [living #{..living1.. ..living2..}]
    (fact "far-separated living cells"
      (fringe living) => #{..1.. ..2.. ..3.. ..4..}
      (provided 
        (surroundings ..living1..) => #{..1.. ..2..}
        (surroundings ..living2..) => #{..3.. ..4..}))

    (fact "two living cells fringe can overlap"
      (fringe living) => #{..1.. ..2.. ..3..}
      (provided 
        (surroundings ..living1..) => #{..1.. ..2..}
        (surroundings ..living2..) => #{..2.. ..3..}))
      
    (fact "one living cell near another one doesn't end up in the fringe"
      (fringe living) => #{..1.. ..2..}
      (provided 
        (surroundings ..living1..) => #{..1.. ..living2..}
        (surroundings ..living2..) => #{..living1.. ..2..}))))

(def combine set/union)



;;; STEP 1
;;; In this, I'm being scrupulous about not assuming any particular
;;; collection representation. I use a generic function `combine` to
;;; avoid committing to, say, `union` or `concat`.

(defn tick [living]
  (combine (survivors living) (births (fringe living) living)))

(fact "a `tick` transforms living and `fringe` cells into another set of living cells"
  (tick ..living..) => ..new-living..
  (provided
    (survivors ..living..) => ..survivors..
    (births (fringe ..living..) ..living..) => ..newborns..
    (combine ..survivors.. ..newborns..) => ..new-living..))


;;; STEP 6
;;; Start with step 1 above.
;;; Once everything was done, here's a final concrete "end-to-end" example:


(fact "blinkers work"
  (tick #{  [0 1] [1 1] [2 1] })
  =>    #{    [1 2]
              [1 1]
              [1 0]})

;;; I note, with a certain amount of smugness, that this test passed
;;; the first time.


;;; Note: I'll often write the end-to-end example first, but in this
;;; case I wanted to defer deciding on representation, so I couldn't
;;; write an end-to-end test that used the final representation.


