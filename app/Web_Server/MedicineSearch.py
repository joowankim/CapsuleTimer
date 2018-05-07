# -*- encoding:utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

import BeautifulSoup
import requests
import json
import pprint

# Method to parse medicine info list from webpage
# Parameter html: HTML page which includes list of medicine user typed
# Return: Parsed information from html using BeautifulSoup
# Information format [ Sequence, Medicine name, Link which leads to specific information, Cheif ingredient, Number of cheif ingredient, Company, Expert/Daily]
# Dependency: Python BeautifulSoap package
def medicine_list_info_parser(html):
    name = [ "Sequence", "name", "link", "ingredient", "ningredient", "company", "type"]
    res = {"medicine":[]}
    soup = BeautifulSoup.BeautifulSoup(html)
    lines = soup.findAll('tr', attrs={'class':'s_result_list'})
    for line in lines:
        res_line = []
        for idx, item in enumerate(line):
            if type(item) != BeautifulSoup.NavigableString:
                res_line.append(item.getText())
            if idx == 3:
                res_line.append(item.findAll('a')[0]['href'])
        res["medicine"].append(dict(zip(name, res_line)))
    return json.dumps(res)

# Method to parse medicine information from webpage
# Parameter html: HTML page which includes information of medicine user typed
# Return: Parsed information from html using BeautifulSoup
# Information format { medicine_name, medicine_effect, medicine_usage, medicine_notice }
# Dependency: Python BeautifulSoap package
def medicine_info_parser(html):
    res = {}
    soup = BeautifulSoup.BeautifulSoup(html)
    res['name'] = soup.findAll('div', attrs={'class': 'main_title'})[0].getText()
    res['effect'] = soup.findAll('div', attrs={'id': 'scroll_03'})[0].findAll('div', attrs={'class':'_preview __doc'})[0].findAll('div')[0].getText()
    res['usage'] = soup.findAll('div', attrs={'id': 'scroll_04'})[0].findAll('div', attrs={'class': '_preview __doc'})[0].findAll('div')[0].getText()
    res['notice'] = soup.findAll('div', attrs={'id': 'scroll_05'})[0].findAll('div', attrs={'class': '_preview'})[0].findAll('div')[0].getText()
    res['image'] = soup.findAll('div', attrs={'class':'main_img_02'})[0].findAll('img')[0]['src']
    return json.dumps(res)


# Receive medicine name from user and return information(list or specific information) of medicine
# Parameter: Name of medicine user wants to search
# Return: Information of medicine
def crawler(medicine_name):
    if '/' not in medicine_name:
        data = {'startCount':'0', 'requery':medicine_name, 'mode':'basic', 'sort':'RANK', 'collection':'kifda', 'item':medicine_name}
        result = requests.post("http://drug.mfds.go.kr/html/search.jsp", data)
        information = medicine_list_info_parser(result.text)
    else:
        result = requests.get("http://drug.mfds.go.kr/html"+medicine_name[1:])
        information = medicine_info_parser(result.text)
    return information
