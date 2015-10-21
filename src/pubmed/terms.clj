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
  "Construct pubmed id-search MESH term for query of MicroRNA mirna-no as a tumor biomarker or anti-cancer agent."
  [mir-no]
  (format "(\"MIRN%s microRNA, human\"[nm] OR \"MIRN-%s microRNA, human\"[nm]) AND (\"Tumor Markers, Biological\"[nm] OR \"Antineoplastic Agents\"[nm])" mir-no mir-no))

(defn term-mirna-biomarker-abstract
  [mir-no]
  (format "(mir-%s[Title] OR microrna-%s[Title] OR mir%s[Title]) AND biomarker[Title/Abstract] AND cancer[Title/Abstract]" mir-no mir-no mir-no))

(def term-cancer-biomarker
  "\"Tumor Markers, Biological\"[nm] OR \"Antineoplastic Agents\"[nm] OR ((biomarker[Title/Abstract] OR therapeutic[Title/Abstract]) AND cancer[Title/Abstract])")

(defn term-mirna-name
  [mir-no]
  ;; mostly mesh has names without dash: "MIRN21 microRNA, human"[nm]
  ;; but also some with dash, eg "MIRN-569 microRNA, human"[nm]
  (format "\"MIRN%s microRNA, human\"[nm] OR \"MIRN-%s microRNA, human\"[nm] OR hsa-mir-%s[Title] OR mir-%s[Title] OR microrna-%s[Title] OR mir%s[Title]" mir-no mir-no mir-no mir-no mir-no mir-no))

(defn term-mirna-biomarker-mesh-or-abstract
  [mir-no]
  (str "(" (term-mirna-name mir-no)
       (if-not (= mir-no (mir-num mir-no)) (str " OR " (term-mirna-name (mir-num mir-no))))
       ") AND (" term-cancer-biomarker ")"))
