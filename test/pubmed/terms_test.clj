(ns pubmed.terms-test
  (:require [clojure.test :refer :all]
            [cemerick.url :as url]
            [pubmed
             [terms :refer :all]
             [core :as pm]]))

(deftest test-a
  (let [x (term-mirna-biomarker-mesh 96)]
    (is (= x (url/url-decode (url/url-encode x))))
    (is (= #cemerick.url.URL{:protocol "http", :username nil, :password nil,
                             :host "eutils.ncbi.nlm.nih.gov", :port -1,
                             :path "/entrez/eutils/esearch.fcgi",
                             :anchor nil,
                             :query {:db "pubmed", :retmode "json",
                                     :term "\"MIRN96 microRNA, human\"[Supplementary Concept] AND \"Tumor Markers, Biological\"[MESH]"}}
           (#'pm/id-search-url x)))))

(deftest term-mir
  (is (= "105" (mir-num "105-1")))
  (is (= "33" (mir-num "33b")))
  (is (= "\"MIRN999 microRNA, human\"[Supplementary Concept] OR mir-999[Title] OR microrna-999[Title] OR mir999[Title] "
         (term-mir-name 999))))
