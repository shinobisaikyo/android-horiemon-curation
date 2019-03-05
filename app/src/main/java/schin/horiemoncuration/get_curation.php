<?php

function getUrlFromNewspicks($article) {
  $html = file_get_contents($article[1]);
  preg_match('/"read-more center"(.*)続きを読む<\/a>/', $html, $matches);
  preg_match('/href="(.*?)"/', $matches[1], $link);
  return array($article[0], $link[1]);
}

$url = "http://horiemon.com/news/";
$html = file_get_contents($url);
preg_match_all('/<a href="(.*)">/', $html, $matches);

foreach ($matches[1] as $match)
{
  if (strpos($match, "horiemon.com/news") !== false) {
    $curation_list[] = $match;
  }
}

$matches = array();
foreach ($curation_list as $curation_page) {
  $html = file_get_contents($curation_page);
  preg_match('/<title>(.*?)<\/title>/i', $html, $title);
  preg_match('/<a href=(.*)記事を読む<\/a>/', $html, $matches);
  preg_match('/<a href="(.*?)"/', $matches[0], $article);
  $articles[] = array($title[1], $article[1]);
}

foreach ($articles as $article) {
  if (strpos($article[1], "newspicks") !== false) {
    $link_list[] = getUrlFromNewspicks($article);
  } else if (strpos($article[1], "twitter") !== false) {
    // no action
  } else if (strpos($article[1], "afpbb") !== false) {
    $link_list[] = $article;
  } else if (strpos($article[1], "jp.techcrunch") !== false) {
    $link_list[] = $article;
  } else {
    // no action
  }
}

header('content-type: application/json; charset=utf-8');
echo json_encode($link_list);
