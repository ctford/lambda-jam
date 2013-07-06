;; http://codingdojo.org/cgi-bin/wiki.pl?KataBankOCR

(ns ocr.core
  (:use midje.sweet)
  (:use ocr.characters))

(unfinished transform-ocr-stream)

(fact "OCR-style account representations can be translated into strings"
  (transform-ocr-stream ["    _  _     _  _  _  _  _ "
                         "  | _| _||_||_ |_   ||_||_|"
                         "  ||_  _|  | _||_|  ||_| _|"
                         "                           "
                         " _  _     _  _  _  _  _  _ "
                         " _| _||_||_ |_   ||_||_|| |"
                         "|_  _|  | _||_|  ||_| _||_|"             
                         "                           "]) => ["123456789" "234567890"])
