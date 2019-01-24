UPDATE price
SET price.price = 110
where price.book_id = 1
and price.quality_id = 1




/* Book price update  */
UPDATE price
SET Price.price = Price.price - 10
WHERE Price.price <= 120
and Price.price > 10

/*                   */

UPDATE price
SET Price.price = Price.price - 15
WHERE Price.Price > 120
and Price.Price <= 200


/*        */

UPDATE price
SET Price.price = Price.price - 20
WHERE Price.price > 200
and Price.price <= 500



UPDATE Price
SET Price.price = Price.price - 30
WHERE Price.price > 500
and Price.price <= 800


UPDATE Price
SET Price.price = Price.price - 40
WHERE Price.price > 800




/*************************************************************************************************************************



                                            PLACING AN ORDER



**************************************************************************************************************************/

/* Inseerting into cartitem  */

INSERT INTO cartitem 
(cartitem.item_id,cartitem.book_id,cartitem.book_order_id,cartitem.price_id,cartitem.promo_id)
VALUES (NULL,1,(SELECT MAX(bookorder.book_order_id) FROM bookorder),1,1)


/*If shipping address is default.  (OEDER placement).  WE CAN GIVE AN OPTION IN THE USEREND. FUTURE PLAN */

INSERT INTO bookorder
(bookorder.book_order_id,bookorder.user_id,bookorder.shipping_address,bookorder.total_cost,bookorder.delivery_confirmed,bookorder.order_issue)
VALUES (NULL,2,(SELECT user.address FROM user where user.user_id = 2),1390,0,NULL)



/*If shipping address is NNNOOOTTTT default.  (OEDER placement)    */

INSERT INTO bookorder
(bookorder.book_order_id,bookorder.user_id,bookorder.shipping_address,bookorder.total_cost,bookorder.delivery_confirmed,bookorder.order_issue)
VALUES (NULL,2,"bosundhora",1390,0,NULL)



/* insert into user */

INSERT INTO user
(user.user_id,user.name,user.email,user.password,user.mobile,user.address,user.created_at)
VALUES(NULL,"INSERT NAME","NULL","NULL","01521420915","Titumir hall 5002,BUET",NULL)





/*************************************************************************************************************************



                                        PLACING AN ORDER QUERY DONE



**************************************************************************************************************************/











/*************************************************************************************************************************



                                        UPDATING AN ORDER QUERY



**************************************************************************************************************************/




/**************************************************


        SUB SECTION : IF ANYONE WANT TO UPDATE SAME ITEM , THEN RUN THIS QUERY

        if update order, then this. number_of_items should be > 0


***************************************************/



UPDATE cartitem
SET cartitem.number_of_item = 5
WHERE cartitem.book_id = 5
and cartitem.book_order_id = 1


/**************************************************


        SUB SECTION : IF ANYONE WANT TO CANCEL ORDER , THEN RUN THIS QUERY


***************************************************/

/*if we want to cancel order */

DELETE FROM cartitem
WHERE cartitem.book_order_id = 1

DELETE FROM bookorder
WHERE bookorder.book_order_id = 1

/* done updating order */



/**************************************************


        SUB SECTION : IF ANYONE WANT TO ADD NEW PRODUCT IN THE SAME ORDER NUMBER , THEN RUN THIS QUERY

        !!!! SHOULD REMEMBER TO ADD AN ELEMENT IN PROMO CODE


***************************************************/

INSERT INTO cartitem 
(cartitem.item_id,cartitem.book_id,cartitem.book_order_id,cartitem.price_id,cartitem.promo_id)
VALUES (NULL,"give book id","give same book_order_id (got from UI)",1,1)

/******                         IMPORTANT NOTE 

            if we want to set price id from UI, then it is fine. IF NOTTTTT, then , code is given below --->>>>


*********/

INSERT INTO cartitem 
(cartitem.item_id,cartitem.book_id,cartitem.book_order_id,cartitem.price_id,cartitem.promo_id)
VALUES (NULL,"give book id","give same book_order_id (got from UI)",(SELECT price.price_id FROM price 
WHERE price.book_id = "value of book_id "
and price.quality_id = " value of quality id"),1)


/*************************************************************************************************************************



                                        UPDATING AN ORDER QUERY DONE



**************************************************************************************************************************/

INSERT INTO cartitem 
                (cartitem.item_id,cartitem.book_id,cartitem.book_order_id,cartitem.price_id,cartitem.promo_id,cartitem.number_of_item)
                VALUES (NULL,'". $bookid."',(SELECT MAX(bookorder.book_order_id) FROM bookorder),
                (SELECT DISTINCT price.price_id FROM price,book where price.book_id = '". $bookid."' and price.quality_id = '". $quality."' ),
                1,'". $quantity."')






DELETE FROM price
WHERE 0 < 
(
    SELECT COUNT(b.book_id) 
    FROM book b
    WHERE 
    (
        SELECT b1.book_id
        FROM book b1
        WHERE b1.book_id = b.book_id
        AND price.book_id = b.book_id
    )
    
)


DELETE FROM price
WHERE 0 < 
(
    SELECT COUNT(b.book_id)
    FROM book b
    WHERE b.book_id = price.book_id
        
)




/*************************************************************************************



                SHOWING ORDERS
SELECT BookOrder.book_order_id ,  User.name, BookOrder.shipping_address,CartItem.number_of_item, BookOrder.total_cost,
BookOrder.order_issue,BookOrder.delivery_confirmed
FROM BookOrder,Book,Author,CartItem,User
WHERE BookOrder.book_order_id=CartItem.book_order_id
AND Book.book_id = CartItem.book_id
AND BookOrder.book_order_id = CartItem.book_order_id
AND User.user_id = BookOrder.user_id
AND BookOrder.delivery_confirmed = 0
GROUP BY BookOrder.book_order_id
ORDER BY BookOrder.book_order_id



***************************************************************************************/

SELECT BookOrder.book_order_id , Book.name ,Author.author_name, CartItem.number_of_item, User.name, BookOrder.order_issue,BookOrder.shipping_address,BookOrder.delivery_confirmed,BookOrder.total_cost
FROM BookOrder,Book,Author,CartItem,User
WHERE BookOrder.book_order_id=CartItem.book_order_id
AND Book.book_id = CartItem.book_id
AND BookOrder.book_order_id = CartItem.book_order_id
AND User.user_id = BookOrder.user_id
GROUP BY BookOrder.book_order_id
ORDER BY BookOrder.book_order_id



SELECT book.name,author.author_name,cartitem.number_of_item,
user.name,bookorder.order_issue,bookorder.shipping_address,
bookorder.delivery_confirmed,bookorder.total_cost
FROM bookorder,book,author,cartitem,user
WHERE bookorder.book_order_id=cartitem.book_order_id
AND book.book_id = cartitem.book_id
AND bookorder.book_order_id = cartitem.book_order_id
AND user.user_id = bookorder.user_id
AND bookorder.book_order_id = 3
GROUP BY book.name