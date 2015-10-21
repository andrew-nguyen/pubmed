(ns pubmed.terms
  (:require [clojure
             [set :as set]
             [string :as str]
             [zip :as zip]
             [pprint :refer :all]
             [edn :as edn]
             [xml :as xml]]
            [clojure.java.io :as io]
            [clojure.data [json :as json]]
            [cemerick.url :as url]))

;;;;;;;;
;; Canned terms for micro-rna cancer biomarker searches
(defn mir-num
  "Return the microrna number part of the mir name, often corresponds to mir family. (eg. 33b -> 33; 105-1 -> 105)"
  [mir]
  (re-find #"^\d+" mir))

(defn term-mirna-biomarker-mesh
  "Construct pubmed id-search term for query of MicroRNA mirna-no as a tumor biomarker."
  [mir-no]
  (format "\"MIRN%s microRNA, human\"[Supplementary Concept] AND \"Tumor Markers, Biological\"[MESH]" mir-no))

(defn term-mir-biomarker-abstract
  [mir-no]
  (format "(mir-%s[Title] OR microrna-%s[Title] OR mir%s[Title]) AND biomarker[Title/Abstract] AND cancer[Title/Abstract]" mir-no mir-no mir-no))

(def term-cancer-biomarker
  "\"Tumor Markers, Biological\"[MESH] OR (biomarker[Title/Abstract] AND cancer[Title/Abstract])")

(defn term-mir-name
  [mir-no]
  (format "\"MIRN%s microRNA, human\"[Supplementary Concept] OR mir-%s[Title] OR microrna-%s[Title] OR mir%s[Title]" mir-no mir-no mir-no mir-no))

(defn term-mir-biomarker-mesh-abstract
  [mir-no]
  (str "(" (term-mir-name mir-no)
       (if-not (= mir-no (mir-num mir-no)) (str " OR " (term-mir-name (mir-num mir-no))))
       ") AND (" term-cancer-biomarker ")"))
