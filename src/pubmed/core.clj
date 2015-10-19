(ns pubmed.core
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

(def ESEARCH  (url/url "http://eutils.ncbi.nlm.nih.gov" "entrez/eutils/esearch.fcgi"))
(def ESUMMARY (url/url "http://eutils.ncbi.nlm.nih.gov" "entrez/eutils/esummary.fcgi"))

(defn term-mirna-biomarker
  "Construct pubmed id-search term for query of MicroRNA mirna-no as a tumor biomarker."
  [mirna-no]
  (format "\"MIRN%s microRNA, human\"[Supplementary Concept] AND \"Tumor Markers, Biological\"[MESH]" mirna-no))

(defn- id-search-url
  [term]
  (assoc ESEARCH :query {:db "pubmed" :retmode "json"
                         :term term}))

(defn- summary-url
  [ids]
  (assoc ESUMMARY :query {:db "pubmed" :retmode "json" :rettype "abstract"
                          :id (if (coll? ids) (str/join \, ids) ids)}))

(defn search-result-count
  [id-search-res]
  (Long/parseLong (get-in id-search-res ["esearchresult" "count"])))

(defn id-list
  [id-search-res]
  (get-in id-search-res ["esearchresult" "idlist"]))

(defn summary-title-dt-id
  "Return seq of [title epubdate id] from result of pubmed summary query."
  [summ-res]
  (map (fn [[id {dt "epubdate" title "title"}]] [title dt id])
       (dissoc (get summ-res "result") "uids")))

(defn id-search
  "Return pubmed id-search for term. Optionally sleep for millis
  milliseconds before querying. NCBI request no more than 3
  queries/sec."
  ([term] (id-search 0 term))
  ([millis term]
   (if (pos? millis) (Thread/sleep millis))
   (json/read (io/reader (str (id-search-url term))))))

(defn summary
  "Return pubmed summary for pubmed id(s). Optionally sleep for millis
  milliseconds before querying. NCBI request no more than 3
  queries/sec."
  ([ids] (summary 0 ids))
  ([millis ids]
   (if (pos? millis) (Thread/sleep millis))
   (json/read (io/reader (str (summary-url ids))))))
