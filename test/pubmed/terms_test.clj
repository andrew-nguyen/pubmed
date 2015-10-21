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
                                     :term "(\"MIRN96 microRNA, human\"[nm] OR \"MIRN-96 microRNA, human\"[nm]) AND (\"Tumor Markers, Biological\"[nm] OR \"Antineoplastic Agents\"[nm])"}}
           (#'pm/id-search-url x)))))

(deftest term-mir
  (is (= "105" (mir-num "105-1")))
  (is (= "33" (mir-num "33b")))
  (is (= "\"MIRN999 microRNA, human\"[nm] OR \"MIRN-999 microRNA, human\"[nm] OR hsa-mir-999[Title] OR mir-999[Title] OR microrna-999[Title] OR mir999[Title]"
         (term-mirna-name 999)))
  (is (= "(\"MIRN999-111 microRNA, human\"[nm] OR \"MIRN-999-111 microRNA, human\"[nm] OR hsa-mir-999-111[Title] OR mir-999-111[Title] OR microrna-999-111[Title] OR mir999-111[Title] OR \"MIRN999 microRNA, human\"[nm] OR \"MIRN-999 microRNA, human\"[nm] OR hsa-mir-999[Title] OR mir-999[Title] OR microrna-999[Title] OR mir999[Title]) AND (\"Tumor Markers, Biological\"[nm] OR \"Antineoplastic Agents\"[nm] OR ((biomarker[Title/Abstract] OR therapeutic[Title/Abstract]) AND cancer[Title/Abstract]))"
         (term-mirna-biomarker-mesh-or-abstract "999-111"))))
