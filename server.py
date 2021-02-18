import flask
import mysql.connector
import sys
import json
from flask import jsonify
from flask_mysqldb import MySQL
from flask import render_template
import pandas as pd
from flask import request
import numpy as np
from scipy import sparse
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import csv
from surprise import NMF, SVD, SVDpp, KNNBasic, KNNWithMeans, KNNWithZScore, CoClustering
from surprise.model_selection import cross_validate
from surprise import Reader, Dataset
from surprise.model_selection import train_test_split
from surprise.model_selection import GridSearchCV

app = flask.Flask(__name__)
app.config['JSONIFY_PRETTYPRINT_REGULAR'] = False
#app.config['MYSQL_USER'] = 'root'
#app.config['MYSQL_PASSWORD'] = 'mertak5656M.'
#app.config['MYSQL_DB'] = 'food'
#app.config['MYSQL_CURSORCLASS'] = 'DictCursor'

#mysql=MySQL(app)




#İstenen yemeğin kaç kez beğenildiği döndüren method
@app.route('/getlike', methods = ['GET', 'POST'])
def getlike():
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True) 
    msg_received2 = flask.request.get_json()
    recipe_id = msg_received2["id"]

    db_cursor.execute("SELECT name FROM recipe where id= " + recipe_id )
    data = db_cursor.fetchall()
    data_as_text = '\n'.join([x[0] for x in data])
    data_as_text = str(data_as_text)
    


    db_cursor.execute("SELECT  COUNT(DISTINCT(user_id)) as number FROM list where name= " + "'" + data_as_text + "'")
    r = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    db_cursor.close()
    chat_db.close()
    return jsonify({'cursor': r})
    
    


#listeden silen rating sıfırlayan method
#buraya list den gelen biligler olduğu için recipe id gelmiyor
#rating yenilemek için recipe_id lamamız lazım
#onu name den çekip alabilirz 
@app.route('/deletelist', methods = ['GET', 'POST'])
def deletelist():
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True) 
    msg_received2 = flask.request.get_json()
    recipe_name = msg_received2["name"]
    user_id = msg_received2["user_id"]
    rating = msg_received2["rating"]
    list_id = msg_received2["id"]


    db_cursor.execute("SELECT id FROM recipe where name= " + "'" + recipe_name + "'")
    r = db_cursor.fetchone()
    words = str(r)
    words = words.strip('()')
    words = words.replace(',', '')



    ###### listeden birden fazla varsa rating update yapma
    select_query = "SELECT * FROM list where user_id = " + "'" + user_id + "'" +  " AND name = "  + "'" + recipe_name + "'"    
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()
    if len(records) > 1:
            select_query = "SELECT * FROM list where user_id = " + "'" + user_id + "'" +  " AND name = "  + "'" + recipe_name + "'"    

    
    else:
        db_cursor.execute("DELETE FROM ratings WHERE user_id=%s AND recipe_id=%s", (user_id, words))
        chat_db.commit()
        

    db_cursor.execute("DELETE FROM list where id = " + list_id)
    chat_db.commit()
    db_cursor.close()
    chat_db.close()
    return words
    











#name verip id alan method
@app.route('/getid', methods = ['GET', 'POST'])
def getid():
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True) 
    msg_received2 = flask.request.get_json()
    recipe_name = msg_received2["name"]
    db_cursor.execute("SELECT id FROM recipe where name= " + "'" + recipe_name + "'")
    r = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    db_cursor.close()
    chat_db.close()
    return jsonify({'cursor': r})



#arama yapma
#collab csv yazan
@app.route('/search', methods = ['GET', 'POST'])
def search():
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True) 
    msg_received2 = flask.request.get_json()
    name = msg_received2["name"]
    db_cursor.execute("SELECT * FROM recipe WHERE name LIKE '%"+ name +  "%'")
    r = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    db_cursor.close()
    chat_db.close()
    return jsonify({'cursor': r})


#collab csv yazan2
@app.route('/collabcsv2', methods = ['GET', 'POST'])
def collabcsv2():
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True) 
    db_cursor.execute("SELECT COUNT(id)  FROM recipe")  
    for row in db_cursor.fetchall():
        count = row[0]
    s=""
    for x in range(1, count+1):
        s+="," + str(x)
    

    db_cursor.execute("select user_id ,group_concat( rating separator ',') as name from ratings  group by user_id")
    rows = db_cursor.fetchall()
    


    #column_names = [i[0] for i in db_cursor.description]
    fp = open('collab_dataset.csv', 'w')
    myFile = csv.writer(fp, lineterminator = '\n')
    myFile.writerow([s])
    myFile.writerows(rows)    
    fp.close()

    text = open("collab_dataset.csv", "r")
    text = ''.join([i for i in text])\
    .replace(",0", ",") 
    x = open("collab_dataset.csv","w")
    x.writelines(text)
    x.close()


    text = open("collab_dataset.csv", "r")
    text = ''.join([i for i in text])\
    .replace("\"0", "") 
    x = open("collab_dataset.csv","w")
    x.writelines(text)
    x.close()


    text = open("collab_dataset.csv", "r")
    text = ''.join([i for i in text]) \
    .replace("\"", "")
    x = open("collab_dataset.csv","w")
    x.writelines(text)
    x.close()

    db_cursor.close()
    chat_db.close()
    return jsonify({'item': s})   

#collab csv yazan
@app.route('/collabcsv', methods = ['GET', 'POST'])
def collabcsv():
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True) 
    db_cursor.execute("SELECT user_id,recipe_id,rating FROM ratings")
    rows = db_cursor.fetchall()
    rows[:] = [(x, y,z) for x, y,z in rows]
        


    column_names = [i[0] for i in db_cursor.description]
    fp = open('collab_dataset.csv', 'w')
    myFile = csv.writer(fp, lineterminator = '\n')
    myFile.writerow(column_names)
    myFile.writerows(rows)    
    fp.close()
    db_cursor.close()
    chat_db.close()
    return jsonify({'item': rows})  



#zerotoall

@app.route('/zero_all', methods = [ 'GET', 'POST'])
def zero_all():
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True) 
    msg_received2 = flask.request.get_json()
    user = msg_received2["user"]
    
    rating = 0

    db_cursor.execute("SELECT COUNT(id)  FROM recipe")
    for row in db_cursor.fetchall():
        count = row[0]
    
    for x in range(1, count+1):

        insert_query = "INSERT INTO ratings ( user_id, recipe_id, rating) VALUES ( %s, %s, %s)"
        insert_values = (user, x, rating)
        try:
            db_cursor.execute(insert_query, insert_values)
            chat_db.commit()
            
        except Exception as e:
            print("Error while inserting the new record :", repr(e))
    db_cursor.close()
    chat_db.close()
    return jsonify({'item': rating})  






#içerik csv yazan
@app.route('/itemcsv', methods = ['GET', 'POST'])
def fetch_table_data():
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True) 
    db_cursor.execute("SELECT id,name,ind FROM recipe")
    rows = db_cursor.fetchall()
    rows[:] = [(x, y,z) for x, y,z in rows]
        


    column_names = [i[0] for i in db_cursor.description]
    fp = open('food_dataset.csv', 'w')
    myFile = csv.writer(fp, lineterminator = '\n')
    myFile.writerow(column_names)
    myFile.writerows(rows)    
    fp.close()
    db_cursor.close()
    chat_db.close()
    return jsonify({'item': rows})  


       

     


#Yeni yemek ekleyen mehtod
@app.route('/addrecipe', methods = ['GET', 'POST'])
def addrecipe():
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True)
    msg_received = flask.request.get_json()
    name = msg_received["name"]
    image = msg_received["image"]
    ind = msg_received["ind"]
    recip = msg_received["recip"]
    user_id = msg_received["user_id"]
    
    insert_query = "INSERT INTO recipe (  name,image,recip,ind) VALUES (  %s, %s,%s,%s)"
    insert_values = ( name,image,recip,ind)
    try:
        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
        
    except Exception as e:
        print("Error while inserting the new record :", repr(e))
        db_cursor.close()
        chat_db.close()
        return "hata"
    db_cursor.execute("SELECT id FROM recipe where name= " + "'" + name + "'")
    r = db_cursor.fetchone()
    recipe_id = str(r)
    recipe_id = recipe_id.strip('()')
    recipe_id = recipe_id.replace(',', '')
    #rating = 0
    rating2 = 5
    

    #yemeği ekleyen yeni yemeği beğeniyor
    insert_query = "INSERT INTO ratings (  user_id,recipe_id,rating) VALUES (  %s, %s,%s)"
    insert_values = (user_id,recipe_id,rating2)
    try:

        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
    except Exception as e:
       print("Error while inserting the new record :", repr(e))

    db_cursor.close()
    chat_db.close()
    return "eklendi"

        


#listeye ekleyen mehtod
@app.route('/additem', methods = ['GET', 'POST'])
def additem():
    msg_received = flask.request.get_json()
    name = msg_received["name"]
    user_id = msg_received["user_id"]
    insert_query = "INSERT INTO basket (  user_id,item) VALUES (  %s, %s)"
    insert_values = ( user_id,name)
    try:
        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
        
    except Exception as e:
        print("Error while inserting the new record :", repr(e))
        return "success"
    
    return "success"



#listedekileri silen mehtod
@app.route('/deleteitem', methods = ['GET', 'POST'])
def deleteitem():
    msg_received = flask.request.get_json()
    id = msg_received["id"]
    db_cursor.execute("DELETE FROM basket where id = " + id)
    chat_db.commit()
    return "success"


#listedekileri getiren mehtod
@app.route('/getitem', methods = ['GET', 'POST'])
def getitem():
    msg_received = flask.request.get_json()
    id = msg_received["user_id"]
    db_cursor.execute("SELECT id,item FROM basket where user_id = " + id)
    r = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    return jsonify({'item': r})




#listeye ekleyen method
@app.route('/addlist', methods = ['GET', 'POST'])
def addlist():
    msg_received = flask.request.get_json()
    id1 = msg_received["id"]
    user_id = msg_received["user_id"]
    db_cursor.execute("SELECT ind FROM recipe where id = " + id1)
    r = db_cursor.fetchall()
    records_as_text = '\n'.join([x[0] for x in r])
    records_as_text = str(records_as_text)
    words = records_as_text.split()
    for word in words:
        insert_query = "INSERT INTO basket (  user_id,item) VALUES (  %s, %s)"
        insert_values = ( user_id,word)
        try:
            db_cursor.execute(insert_query, insert_values)
            chat_db.commit()
        
        except Exception as e:
            print("Error while inserting the new record :", repr(e))
    return "success"

@app.route('/foodlist', methods = ['GET', 'POST'])
def foodlist():
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True)
    msg_received = flask.request.get_json()
    id = msg_received["id"]

    db_cursor.execute("SELECT * FROM list where user_id = " + id + " ORDER BY date DESC")
    r = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    db_cursor.close()
    chat_db.close()
    return jsonify({'cursor': r})


#geçmiştekini bir daha ekleyen metod
@app.route('/againlist', methods = [ 'POST'])
def again_add_food():
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True)
    msg_received2 = flask.request.get_json()
    name = msg_received2["name"]
    image = msg_received2["image"]
    user_id = msg_received2["user_id"]
    insert_query = "INSERT INTO list ( name, image,user_id) VALUES ( %s, %s, %s)"
    insert_values = (name, image,user_id)
    try:
        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
        db_cursor.close()
        chat_db.close()
        return "success"
    except Exception as e:
        print("Error while inserting the new record :", repr(e))
        db_cursor.close()
        chat_db.close()
        return "failure"








#tavsiyeden geçmişe ekleyen metod
@app.route('/list', methods = [ 'POST'])
def add_food():
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True)

    msg_received2 = flask.request.get_json()
    name = msg_received2["name"]
    image = msg_received2["image"]
    user_id = msg_received2["user_id"]
    rating = msg_received2["rating"]
    id2 = msg_received2["id"]

    select_query = "SELECT * FROM ratings where user_id = " + "'" + user_id + "'" +  " AND recipe_id = "  + "'" + id2 + "'"    
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()
    if len(records) != 0:
        db_cursor.execute("UPDATE ratings SET rating=%s WHERE user_id=%s AND recipe_id=%s", (rating, user_id, id2))
        chat_db.commit()
    
    else:
        insert_query = "INSERT INTO ratings ( user_id, recipe_id, rating) VALUES ( %s, %s, %s)"
        insert_values = (user_id, id2, rating)
        try:
            db_cursor.execute(insert_query, insert_values)
            chat_db.commit()
        except Exception as e:
            print("Error while inserting the new record :", repr(e))
    

    insert_query = "INSERT INTO list ( name, image,user_id) VALUES ( %s, %s, %s)"
    insert_values = (name, image,user_id)
    try:
        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
        db_cursor.close()
        chat_db.close()
        return "success"
    except Exception as e:
        print("Error while inserting the new record :", repr(e))
        db_cursor.close()
        chat_db.close()
        return "failure"


@app.route('/rating', methods = [ 'POST'])
def rating_food():
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True)

    msg_received2 = flask.request.get_json()
    user = msg_received2["user"]
    recipe = msg_received2["recipe"]
    rating = msg_received2["rating"]
    user = str(user)
    recipe = str(recipe)

    select_query = "SELECT * FROM ratings where user_id = " + "'" + user + "'" +  " AND recipe_id = "  + "'" + recipe + "'"    
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()
    if len(records) != 0:
        db_cursor.execute("UPDATE ratings SET rating=%s WHERE user_id=%s AND recipe_id=%s", (rating, user, recipe))
        chat_db.commit()
        return "success"

    insert_query = "INSERT INTO ratings ( user_id, recipe_id, rating) VALUES ( %s, %s, %s)"
    insert_values = (user, recipe, rating)
    try:
        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
        db_cursor.close()
        chat_db.close()
        return "success"
    except Exception as e:
        print("Error while inserting the new record :", repr(e))
        db_cursor.close()
        chat_db.close()
        return "failure"


@app.route('/food', methods = ['GET', 'POST'])
def get_all_food():
    db_cursor.execute("SELECT * FROM recipe")
    r = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    return jsonify({'cursor': r})

@app.route('/fooddetail', methods = ['GET', 'POST'])
def get_food():
    msg_received = flask.request.get_json()
    id = msg_received["id"]
    

    db_cursor.execute("SELECT * FROM recipe where id = " + id)
    r = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    return jsonify({'cursor': r})


@app.route('/', methods = ['GET', 'POST'])
def chat():
    msg_received = flask.request.get_json()
    msg_subject = msg_received["subject"]

    if msg_subject == "register":
        return register(msg_received)
    elif msg_subject == "login":
        return login(msg_received)
    elif msg_subject == "item":
        return item(msg_received)
    else:
        return "Invalid request."

def register(msg_received):
    username = msg_received["username"]
    password = msg_received["password"]
    status = msg_received["status"]

    select_query = "SELECT * FROM users where username = " + "'" + username + "'"
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()
    if len(records) != 0:
        return "Another user used the username. Please chose another username."

    insert_query = "INSERT INTO users ( username, password,status) VALUES ( %s, MD5(%s), %s)"
    insert_values = (username, password,status)
    try:
        db_cursor.execute(insert_query, insert_values)
        chat_db.commit()
        return "success"
    except Exception as e:
        print("Error while inserting the new record :", repr(e))
        return "failure"
		
###### yardımcı metodlar isimden index bulma ve indextenten name bulma
def get_title_from_index(id):
    df = pd.read_csv("food_dataset.csv")
    features = ['ind']
    df["combine_features"] = df.apply(combine_features,axis=1) 
    #print (df["combine_features"].head())
    ## Combine sutününndan yeni bir matrix ouşturulması
    cv = CountVectorizer()

    count_matrix = cv.fit_transform(df["combine_features"])

    ##Cosünüs benzerliği burada yapılır.
    cosine_sim = cosine_similarity(count_matrix)
    return df[df.id == id]["name"].values[0]

def get_index_from_title(name):
    df = pd.read_csv("food_dataset.csv")
    features = ['ind']
    df["combine_features"] = df.apply(combine_features,axis=1) 
    #print (df["combine_features"].head())
    ##Combine sutününndan yeni bir matrix ouşturulması
    cv = CountVectorizer()

    count_matrix = cv.fit_transform(df["combine_features"])

    ##Cosünüs benzerliği burada yapılır.
    cosine_sim = cosine_similarity(count_matrix)
    return df[df.name == name]["id"].values[0]

def combine_features(row):
    return row['ind']
		
@app.route('/item', methods = ['GET', 'POST'])		
def item():
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True)   
    msg_received = flask.request.get_json()
    id6 = msg_received["id"]
    df = pd.read_csv("food_dataset.csv")
    df['ind'] = df.ind.str.split(' ')

    #İlk durumumuzda içerik bilgilerini kullanmamıza gerek yok, onlarsın yeni veriye kopyaladım.
    ind_df = df.copy()

    #DAtaframe her satır için,içerik için karşılık gelen sütuna bir 1 yerleştirilmesi
    for index, row in df.iterrows():
        for inde in row['ind']:
            ind_df.at[index, inde] = 1
        
    #Bir yemek o içeriğe sahip değilse NA yerine 0 koymak
    ind_df = ind_df.fillna(0)   

    db_cursor.execute("select name, rating from ratings,recipe where ratings.user_id=" + id6 + " AND ratings.recipe_id=recipe.id")
    r = [dict((db_cursor.description[i][0], value)
            for i, value in enumerate(row)) for row in db_cursor.fetchall()]

    inputFood = pd.DataFrame(r)
    inputFood



    #Yemeklerin isimlere göre filtrelenmesi
    inputId = df[df['name'].isin(inputFood['name'].tolist())]

    #Birleştirme işlemi
    inputFood = pd.merge(inputId, inputFood)


    #Filtreleme
    userFood = ind_df[ind_df['id'].isin(inputFood['id'].tolist())]


    #DAtaframe sıfırlama
    userFood = userFood.reset_index(drop=True)

    #Gereksiz sutünları çıkarma
    userIndTable = userFood.drop('id', 1).drop('ind', 1).drop('name', 1)


    #Kullanıcı profili

    userProfile = userIndTable.transpose().dot(inputFood['rating'])




    #Yemeklerin her içerini dataframe almak
    indTable = ind_df.set_index(ind_df['id'])

    #Gereksiz yerleri çıkarmak
    indTable = indTable.drop('id', 1).drop('name', 1).drop('ind', 1)

    indTable.shape

    #İçerikleri ağırlıklarla çarpılması ve  ağırlıklı ortalamayı alınması
    recommendationTable_df = ((indTable*userProfile).sum(axis=1))/(userProfile.sum())


    #Tavsiyeleri sıralama
    recommendationTable_df = recommendationTable_df.sort_values(ascending=False)

    
    lenght = len(recommendationTable_df)
    a = []
    j=0
    b = 2
    s=[]


    for x in range(0, lenght-5):
        #listede varmı diye kontrol
        b = 3
        name2 =get_title_from_index(recommendationTable_df.index[x])
        name2 = str(name2)
        db_cursor.execute("SELECT id FROM recipe where name= " + "'" + name2 + "'")
        r = db_cursor.fetchone()
        words = str(r)
        words = words.strip('()')
        words = words.replace(',', '')
        select_query = "SELECT * FROM list where user_id = " + "'" + id6 + "'" + " AND name = " + "'" + name2 + "'"
        db_cursor.execute(select_query)
        records = db_cursor.fetchall() 
        if len(records) == 1:
            status = 1
            j=0
            db_cursor.execute("SELECT name FROM list where user_id = " + id6 + " Order by id desc")
            r = db_cursor.fetchall()
            records_as_text = '\n'.join([x[0] for x in r])
            records_as_text = str(records_as_text)
            words = records_as_text.split()
            while j<len(words) and status !=0:
                if(words[j] == name2 and b<j<b+4):
                   
                    db_cursor.execute("SELECT * FROM recipe where name = " + "'" + name2 + "'")
                    r = [dict((db_cursor.description[i][0], value)
                            for i, value in enumerate(row)) for row in db_cursor.fetchall()]
                    a.extend(r)
                    status = 0
                elif(words[j] == name2 and b<j):
                    db_cursor.execute("SELECT * FROM recipe where name = " + "'" + name2 + "'")
                    r = [dict((db_cursor.description[i][0], value)
                            for i, value in enumerate(row)) for row in db_cursor.fetchall()]
                    s.extend(r)
                    status = 0
                elif(words[j]== name2):
                    status = 0
                else:
                    j = j + 1

                    
        elif len(records) > 1:
            c=1
            j=0
            d=0
            i=0
            db_cursor.execute("SELECT name FROM list where user_id = " + id6 + " Order by id desc")
            r = db_cursor.fetchall()
            
            records_as_text = '\n'.join([x[0] for x in r])
            records_as_text = str(records_as_text)
            
            words = records_as_text.split()
             
            for word in words:
                if(word == name2 and c==1 and d==0):
                    c=0
                    i=0
                    d=1
                elif(word == name2 and c!=1 and d==1):
                    c = c + i
                    i = 0
                elif(d==0):
                    d=0
                else:
                    i = i + 1

            c = c / (len(records) - 1)
            c = int (c)
            j=0

            db_cursor.execute("SELECT name FROM list where user_id = " + id6 + " Order by id desc")
            r = db_cursor.fetchall()
            status=1
            records_as_text = '\n'.join([x[0] for x in r])
            records_as_text = str(records_as_text)
            words = records_as_text.split()
            while j<len(words) and status !=0:
                if(words[j]  == name2 and c<j<c+4):
                    
                    db_cursor.execute("SELECT * FROM recipe where name = " + "'" + name2 + "'")
                    r = [dict((db_cursor.description[i][0], value)
                            for i, value in enumerate(row)) for row in db_cursor.fetchall()]
                    a.extend(r)
                    status = 0
                elif(words[j] == name2 and c<j):
                    db_cursor.execute("SELECT * FROM recipe where name = " + "'" + name2 + "'")
                    r = [dict((db_cursor.description[i][0], value)
                            for i, value in enumerate(row)) for row in db_cursor.fetchall()]
                    s.extend(r)
                    status = 0
                elif(words[j]  == name2):
                    status = 0
                else:
                    j = j + 1

          
        

        else:
            name2 =get_title_from_index(recommendationTable_df.index[x])
            db_cursor.execute("SELECT * FROM recipe where name = " + "'" + name2 + "'")
            r = [dict((db_cursor.description[i][0], value)
                    for i, value in enumerate(row)) for row in db_cursor.fetchall()]
            a.extend(r)
    
    
    a.extend(s)
    db_cursor.close()
    chat_db.close()
    return jsonify({'cursor': a})   

def standardize(row):
    new_row = (row - row.mean())/(row.max()-row.min())
    return new_row
def get_similar(food_name,rating):
    ratings = pd.read_csv("collab_dataset.csv",index_col=0)
    ratings = ratings.fillna(0)
    ratings_std = ratings.apply(standardize)


    item_similarity = cosine_similarity(ratings_std.T)
    item_similarity_df= pd.DataFrame(item_similarity,index=ratings.columns,columns=ratings.columns)
    similar_score = item_similarity_df[food_name]*(rating)
    similar_score = similar_score.sort_values(ascending=False)
    #print(type(similar_ratings))
    return similar_score


@app.route('/collab', methods = ['GET', 'POST'])
def collab():
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True)
    msg_received = flask.request.get_json()
    id6 = msg_received["id"]
    sayi = int(id6)

    db_cursor.execute("select user_id,recipe_id,rating from ratings where ratings.user_id=" + id6 )
    r = [dict((db_cursor.description[i][0], value)
            for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    
    my_ratings = pd.DataFrame(r) 
    #my_ratings = str(my_ratings)
    #print(my_ratings)
    
    

    food = pd.read_csv("food_dataset.csv")

    ratings = pd.read_csv("collab_dataset.csv")

    df = pd.merge(ratings, food, left_on ='recipe_id', right_on='id').drop(['ind'], axis=1)

    df = df.drop(['name'], axis=1)

    df = df.drop(['id'], axis=1)

    combined_food_data = pd.concat([df, my_ratings], axis=0)
    # rename the columns to userID, itemID and rating
    #print(combined_food_data)
    combined_food_data.columns = ['userID', 'itemID', 'rating']

    # Gruplama ile kaç kullanıcı ona puan vermiş öğrenme ve filtreleme yapmak
    combined_food_data['reviews'] = combined_food_data.groupby(['itemID'])['rating'].transform('count')
    combined_food_data= combined_food_data[combined_food_data.reviews>1][['userID', 'itemID','rating']]

    reader = Reader(rating_scale=(1, 5))
    data = Dataset.load_from_df(combined_food_data, reader)


    
    unique_ids = combined_food_data['itemID'].unique()
    # kullanıcı adına göre ratingleri al
    iids1001 = combined_food_data.loc[combined_food_data['userID']==sayi, 'itemID']
    # Puanladığı filmleri tavsiye siteminden silme
    food_to_predict = np.setdiff1d(unique_ids,iids1001)
    param_grid = {'n_factors': [30], 'n_epochs': [15], 'lr_all': [0.005],'reg_all': [0.1]}
    gs = GridSearchCV(SVD, param_grid, measures=['rmse'], cv=3)
    gs.fit(data)
    algo = gs.best_estimator['rmse']


    #Assigning values
    t = gs.best_params
    factors = t['rmse']['n_factors']
    epochs = t['rmse']['n_epochs']
    lr_value = t['rmse']['lr_all']
    reg_value = t['rmse']['reg_all']



    trainset, testset = train_test_split(data, test_size=0.25)

    algo = SVD(n_factors=factors, n_epochs=epochs, lr_all=lr_value, reg_all=reg_value)
    algo.fit(trainset).test(testset)
    my_recs = []
    for iid in food_to_predict:
        my_recs.append((iid, algo.predict(uid=sayi,iid=iid).est))
    
    aa =pd.DataFrame(my_recs, columns=['iid', 'predictions']).sort_values('predictions', ascending=False)
    #print(pd.DataFrame(my_recs, columns=['iid', 'predictions']).sort_values('predictions', ascending=False).head(10))
    rec = []  
    a = []
    for i in range(0,len(aa.index)):
        f = aa.loc[aa.index[i], 'iid']
        rec.append(f)
        #print(f)
   

    #print(rec)
    i=0
    n =3
    while(i<n):
        item= str(rec[i])
        db_cursor.execute("SELECT * FROM recipe where id = " + "'" + item + "'")
        r = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]
        a.extend(r) 
        i = i + 1
    
    #for item in rec:
    #    item = str(item)
    #    db_cursor.execute("SELECT * FROM recipe where id = " + "'" + item + "'")
    #    r = [dict((db_cursor.description[i][0], value)
    #            for i, value in enumerate(row)) for row in db_cursor.fetchall()]
    #    a.extend(r) 

    
    db_cursor.close()
    chat_db.close()
    return jsonify({'cursor': a})   

def login(msg_received):
    try:
        chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
    except:
        sys.exit("Error connecting to the database. Please check your inputs.")
    db_cursor = chat_db.cursor(buffered=True)
    username = msg_received["username"]
    password = msg_received["password"]

    

    select_query = "SELECT username, password FROM users where username = " + "'" + username + "' and password = " + "MD5('" + password + "')"
    db_cursor.execute(select_query)
    records = db_cursor.fetchall()

    

    if len(records) == 0:
        return "failure"
    else:
        select_query2 = "SELECT id,status FROM users where username = " + "'" + username + "' and password = " + "MD5('" + password + "')"
        db_cursor.execute(select_query2)
        r2 = [dict((db_cursor.description[i][0], value)
                for i, value in enumerate(row)) for row in db_cursor.fetchall()]

        db_cursor.execute("UPDATE users SET status=%s where username = %s and password = MD5(%s)",(username,username,password))
        chat_db.commit()
        db_cursor.close()
        chat_db.close()
        return jsonify({'data': r2})
try:
    chat_db = mysql.connector.connect(host="localhost", user="root", passwd="mertak5656M.", database="food")
except:
    sys.exit("Error connecting to the database. Please check your inputs.")
db_cursor = chat_db.cursor(buffered=True)
app.run(host="127.0.0.1", port=5000, debug=True, threaded=True)