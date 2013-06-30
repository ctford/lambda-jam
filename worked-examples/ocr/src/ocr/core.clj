;; http://codingdojo.org/cgi-bin/wiki.pl?KataBankOCR

(ns ocr.core
  (:use midje.sweet)
  (:use ocr.characters))

(defn split-ocr-into-digits [ocr]
  (apply map vector (map (partial re-seq #"...") ocr)))


(fact
  (split-ocr-into-digits ["123---"
                          "456+++"]) => [ ["123"
                                           "456"] ["---"
                                                   "+++"]])

(defn transform-ocr-digit [ocr-digit]
  (str (character-representations ocr-digit)))

(fact 
  (transform-ocr-digit ["   "
                        "  |"
                        "  |"
                        "   "]) => "1")


(defn transform-ocr [ocr]
  (apply str (map transform-ocr-digit (split-ocr-into-digits ocr))))

(fact "OCRs are translated by translating their constituent ocr-digits"
  (transform-ocr ..ocr..) => "12"
  (provided
    (split-ocr-into-digits ..ocr..) => [..digit-1.. ..digit-2..]
    (transform-ocr-digit ..digit-1..) => "1"
    (transform-ocr-digit ..digit-2..) => "2"))

(defn split-ocr-stream [ocr-stream]
  (partition 4 ocr-stream))

(fact "an ocr stream consists of four-line ocrs"
  (split-ocr-stream ["1" "2" "3" "4" "5" "6" "7" "8"]) => [ ["1" "2" "3" "4"] ["5" "6" "7" "8"]])


(defn transform-ocr-stream [stream]
  (map transform-ocr (split-ocr-stream stream)))

(fact "OCR streams are translated by translating their constituent ocrs"
  (transform-ocr-stream ..ocr-stream..) => ["12" "23"]
  (provided
    (split-ocr-stream ..ocr-stream..) => [..ocr-1.. ..ocr-2..]
    (transform-ocr ..ocr-1..) => "12"
    (transform-ocr ..ocr-2..) => "23"))
    

(fact "OCR-style account representations can be translated into strings"
  (transform-ocr-stream ["    _  _     _  _  _  _  _ "
                         "  | _| _||_||_ |_   ||_||_|"
                         "  ||_  _|  | _||_|  ||_| _|"
                         "                           "
                         " _  _     _  _  _  _  _  _ "
                         " _| _||_||_ |_   ||_||_|| |"
                         "|_  _|  | _||_|  ||_| _||_|"             
                         "                           "]) => ["123456789" "234567890"])

