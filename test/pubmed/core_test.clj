(ns pubmed.core-test
  (:require [clojure.test :refer :all]
            [pubmed.core :refer :all :as pm]
            [cemerick.url :as url]))

(deftest test-a
  (let [x (term-mirna-biomarker 96)]
    (is (= x (url/url-decode (url/url-encode x))))
    (is (= #cemerick.url.URL{:protocol "http", :username nil, :password nil,
                             :host "eutils.ncbi.nlm.nih.gov", :port -1,
                             :path "/entrez/eutils/esearch.fcgi",
                             :anchor nil,
                             :query {:db "pubmed", :retmode "json",
                                     :term "\"MIRN96 microRNA, human\"[Supplementary Concept] AND \"Tumor Markers, Biological\"[MESH]"}}
           (#'pm/id-search-url x)))))

(deftest test-url+url-encode
  (is (= "http://a.b.com/some/path?bob=fred"
         (str (assoc (url/url "http://a.b.com" "some/path")
                     :query {:bob "fred"}))))
  (is (= "http://a.b.com/some/path?jim=bob"
         (str (assoc (url/url "http://a.b.com" "some/path")
                     :query {:jim "bob"}))))
  (is (= "http://a.b.com/some/path?jim=%22bob%22"
         (str (assoc (url/url "http://a.b.com" "some/path")
                     :query {:jim "\"bob\""}))))
  (is (= "http://a.b.com/some/path?bob=%22jim%22%5Bbob%5D"
         (str (assoc (url/url "http://a.b.com" "some/path")
                     :query {:bob "\"jim\"[bob]"})))))

(deftest test-b
  (is (= #cemerick.url.URL{:protocol "http", :username nil, :password nil,
                           :host "eutils.ncbi.nlm.nih.gov", :port -1,
                           :path "/entrez/eutils/esummary.fcgi",
                           :anchor nil,
                           :query {:db "pubmed", :retmode "json", :rettype "abstract",
                                   :id 999}}
         (#'pm/summary-url 999)))
  (is (= #cemerick.url.URL{:protocol "http", :username nil, :password nil,
                           :host "eutils.ncbi.nlm.nih.gov", :port -1,
                           :path "/entrez/eutils/esummary.fcgi",
                           :anchor nil,
                           :query {:db "pubmed", :retmode "json", :rettype "abstract",
                                   :id "999,222,1234"}}
         (#'pm/summary-url [999 222 1234]))))

(deftest test-summary-title
  (is (= [ ["Expression of microRNA-96 and its potential functions by targeting FOXO3 in non-small cell lung cancer."
            "2014 Oct 7"
            "25286764"]
           ["miR‑96 functions as a tumor suppressor gene by targeting NUAK1 in pancreatic cancer."
            "2014 Sep 19"
            "25242509"]]
         (summary-title-dt-id {"result" {"25286764" {"title"
                                                     "Expression of microRNA-96 and its potential functions by targeting FOXO3 in non-small cell lung cancer.",
                                                     "epubdate" "2014 Oct 7"},
                                         "25242509" {"title"
                                                     "miR‑96 functions as a tumor suppressor gene by targeting NUAK1 in pancreatic cancer.",
                                                     "epubdate" "2014 Sep 19"}}}))))
