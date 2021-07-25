# transaction-simple

# this project, sample test, to use jpa transaction for save-update and query, to used for change state of data.

1- mind your step, when enable clearAutomatically, maybe have sideEffect, because, cache of jpa is clear and auto flush <br>
2- when you want update entity dont need jpa.save (anti-pattern) <br>
3- you should use @version for entity, because if you dont use @version, entityManager must be check entity with database
