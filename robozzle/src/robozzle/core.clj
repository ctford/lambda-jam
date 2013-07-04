(ns robozzle.core
  (use midje.sweet))

;; TO AUTOTEST (see top-level README.txt for more detail)
;; % lein repl
;; user> (use 'midje.repl)
;; user> (autotest) 

(defn win? [& {start :start, moves :moves, stars :goal-stars move-count :within}]
  false)

(fact "It works end-to-end." 
  (win? :start :FORMAT-TO-BE-DETERMINED
        :moves [:MOVE1 :MOVE2 :MOVE3...]
        :goal-stars [:STAR1 :STAR2 :STAR3...]
        :within 10000) => truthy

  (win? :start :FORMAT-TO-BE-DETERMINED
        :moves []
        :goal-stars [:STAR1]
        :within 10000) => falsey
  ;; maybe other tests
)
