(define (domain sokoban-domain)
  (:requirements :equality)
  (:predicates (has_player ?x)
               (has_box ?x)
               (adjacent ?x ?y)
               (adjacent_2 ?x ?y))
  (:action move-player
           :parameters (?x ?y)
           :precondition (and (adjacent ?x ?y)
                              (not (has_box ?y))
                              (has_player ?x))
           :effect		   (and (has_player ?y)
                              (not (has_player ?x))))
  (:action push-box
		   :parameters (?x ?y ?z)
		   :precondition (and (has_player ?x)
                          (has_box ?y)
                          (not (has_box ?z))
                          (adjacent ?x ?y)
                          (adjacent ?y ?z)
                          (adjacent_2 ?x ?z))
	       :effect (and (has_box ?z)
						(not(has_box ?y))
						(has_player ?y)
						(not(has_player ?x))))
  )