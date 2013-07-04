(ns robozzle.core
  (:use midje.sweet clojure.set))

;;; I have a video of how I arrived at the code below. 
;;; https://vimeo.com/19404746



;;; I didn't use the keywords directly in order to show just how long
;;; you can defer choosing concrete data structures.

(def north :north)
(def south :south)
(def east :east)
(def west :west)

(defn snapshot [direction position] [direction position])
(defn position [snapshot] (second snapshot))
(defn direction [snapshot] (first snapshot))
(def displacement {north [0 1] east [-1 0] south [0 -1] west [1 0]})


(defn forward [current]
  (snapshot (direction current)
            (map +
                 (displacement (direction current))
                 (position current))))
                 
(fact "forward changes the position, but not the direction"
  (forward (snapshot north [5, 50])) => (snapshot north [5, 51])
  (forward (snapshot east [5, 50])) => (snapshot east [4, 50])
  (forward (snapshot south [5, 50])) => (snapshot south [5, 49])
  (forward (snapshot west [5, 50])) => (snapshot west [6, 50]))

(def left-rotation {north east, east south, south west, west north})
(def right-rotation {north west, east north, south east, west south})

(defn rotate [rotation-type current]
  (snapshot (rotation-type (direction current))
            (position current)))
(def left (partial rotate left-rotation))
(def right (partial rotate right-rotation))

;;; Notice below that I got east and west reversed here. These facts,
;;; alas, only check that I did what I intended to do, not that I
;;; intended the right thing.
                 
(fact "left changes the direction, but not the position"
  (left (snapshot north ...position...)) => (snapshot east ...position...)
  (left (snapshot east ...position...)) => (snapshot south ...position...)
  (left (snapshot south ...position...)) => (snapshot west ...position...)
  (left (snapshot west ...position...)) => (snapshot north ...position...))

(fact "right changes the direction, but not the position"
  (right (snapshot north ...position...)) => (snapshot west ...position...)
  (right (snapshot east ...position...)) => (snapshot north ...position...)
  (right (snapshot south ...position...)) => (snapshot east ...position...)
  (right (snapshot west ...position...)) => (snapshot south ...position...))

(defn trail [voyage]
  (map position voyage))

 (fact "a trail is a sequence of positions from a voyage"
  (trail []) => []
  (trail [ (snapshot .unimportant. ...first...)
          (snapshot .unimportant. ...second...) ]) => [...first... ...second... ])

(defn voyage [start moves]
  (reduce (fn [so-far move]
            (conj so-far (move (last so-far))))
          [start]
          moves))

(fact "a voyage is created by successively applying moves to snapshots"
  (voyage ...start... []) => [...start...]
  (voyage ...start... [...move...])
  => [...start... ...next...]
  (provided (...move... ...start...) => ...next...))
  

(defn win? [& {start :start, moves :moves, stars :goal-stars move-count :within}]
  (subset? (set stars)
           (set (take (inc move-count)
                      (trail (voyage start moves))))))

(fact "It works end-to-end." 
  ;;; This was the first test written.
  (let [start (snapshot north [0 0])
        winning-moves     [left forward right right forward forward]
        moves-stop-short  [left forward right right forward]
        misses-star-moves [left forward right right forward right]
        goal [ [-1, 0] [1 0]] ]
    (win? :start start 
          :moves winning-moves
          :goal-stars goal
          :within 5000000) => truthy
    (win? :start start 
          :moves moves-stop-short
          :goal-stars goal
          :within 5000000) => falsey
    (win? :start start 
          :moves misses-star-moves
          :goal-stars goal
          :within 500000) => falsey

    (win? :start start 
          :moves winning-moves
          :goal-stars goal
          :within 6) => truthy

    (win? :start start 
          :moves winning-moves
          :goal-stars goal
          :within 5) => falsey))
