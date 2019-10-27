%:- use_module(library(clpfd)).



% Function to get the list of all starters with price
get_starter(X, Y) :-
	X = ['Corn Tikki',
		 'Tomato Soup',
		 'Chilli Paneer',
		 'Crispy chicken',
		 'Papdi Chaat',
		 'Cold Drink'],
	Y = [30, 20, 40, 40, 20, 20].

% Function to get the list of all main course dishes with price
get_main_dish(X, Y) :-
	X = ['Kadhai Paneer with Butter / Plain Naan',
		 'Veg Korma with Butter / Plain Naan',
		 'Murgh Lababdar with Butter / Plain Naan',
		 'Veg Dum Biryani with Dal Tadka',
		 'Steam Rice with Dal Tadka'],
	Y = [50, 40, 50, 50, 40].

% Function to get the list of all deserts with price
get_desert(X, Y) :-
	X = ['Ice-cream',
		 'Malai Sandwich',
		 'Rasmalai'],
	Y = [20, 30, 10].



% as defined in the problem statement
menu(Str, X, Y, Z) :-
	((Str = 'hungry') -> hungry(X, Y, Z) ; 
	((Str = 'not so hungry') -> not_so_hungry(X, Y, Z) ;
	((Str = 'diet') -> diet(X, Y, Z)))).

% as defined in the problem statement
find_items(Str, X, Y, Z) :-
	((Str = 'hungry') -> hungry_items(X, Y, Z) ; 
	((Str = 'not so hungry') -> not_so_hungry_items(X, Y, Z) ;
	((Str = 'diet') -> diet_items(X, Y, Z)))).



% Check if the value of X, Y and Z are correct
% and print a possible combination of dishes
% in case I am hungry
hungry_items(X, Y, Z) :-
	hungry(X, Y, Z),
	get_starter(X1, _),
	[A1|_] = X1,
	get_main_dish(X2, _),
	[A2|_] = X2,
	get_desert(X3, _),
	[A3|_] = X3,
	write("Items: "),
	writeln(A1),
	write("Items: "),
	writeln(A2),
	write("Items: "),
	writeln(A3).

% Check if the value of X, Y and Z are correct
% and print a possible combination of dishes
% in case I am not so hungry
% Case X=1, Y=1, Z=0
not_so_hungry_items(X, Y, Z) :-
	X is 1,
	Y is 1,
	Z is 0,
	get_starter(X1, Y1),
	min_in_list(Y1, X1, Min1, Val1),
	get_main_dish(X2, Y2),
	min_in_list(Y2, X2, Min2, Val2),
	Sum is Min1 + Min2,
	Sum =< 80,
	write("Items: "), writeln(Val1),
	write("Items: "), writeln(Val2).

% Case X=0, Y=1, Z=1
not_so_hungry_items(X, Y, Z) :-
	X is 0,
	Y is 1,
	Z is 1,
	get_main_dish(X2, Y2),
	min_in_list(Y2, X2, Min2, Val2),
	get_desert(X3, Y3),
	min_in_list(Y3, X3, Min3, Val3),
	Sum is Min2 + Min3,
	Sum =< 80,
	write("Items: "), writeln(Val2),
	write("Items: "), writeln(Val3).

% Check if the value of X, Y and Z are correct
% and print a possible combination of dishes
% in case I am on diet
% Case X=1, Y=0, Z=0
diet_items(X, Y, Z) :-
	X is 1,
	Y is 0,
	Z is 0,
	MaxVal is 40,
	get_starter(X1, Y1),
	min_in_list(Y1, X1, Min1, _),
	Min1 =< MaxVal,
	length(X1, Len1),
	find(Len1, X1, Y1, MaxVal).

% Case X=0, Y=1, Z=0
diet_items(X, Y, Z) :-
	X is 0,
	Y is 1,
	Z is 0,
	MaxVal is 40,
	get_main_dish(X2, Y2),
	min_in_list(Y2, X2, Min2, _),
	Min2 =< MaxVal,
	length(X2, Len2),
	find(Len2, X2, Y2, MaxVal).

% Case X=0, Y=0, Z=1
diet_items(X, Y, Z) :-
	X is 0,
	Y is 0,
	Z is 1,
	MaxVal is 40,
	get_desert(X3, Y3),
	min_in_list(Y3, X3, Min3, _),
	Min3 =< MaxVal,
	length(X3, Len3),
	find(Len3, X3, Y3, MaxVal).

% X contains names of dishes and Y contains corresponding prices
% Len is length of X and Y
% prints items in X such that total price does no exceed Left
find(Len, X, Y, Left) :-
	[A1|B1] = X,
	[A2|B2] = Y,
	Len1 is Len-1,
	((A2 =< Left) -> (write("Items: "), writeln(A1), Left1 is Left-A2,	% Print the item if its prices is not greater than Left
					 ((Len1 > 0) -> find(Len1, B1, B2, Left1) ;			% and subtract the same amount from Left
					 				write(""))) ;
					 ((Len1 > 0) -> find(Len1, B1, B2, Left) ;
					 				write(""))).


hungry(X, Y, Z) :-
	X is 1,
	Y is 1,
	Z is 1.

not_so_hungry(X, Y, Z) :-
	X is 1,
	Y is 1,
	Z is 0.

not_so_hungry(X, Y, Z) :-
	X is 0,
	Y is 1,
	Z is 1.

diet(X, Y, Z) :-
	X is 1,
	Y is 0,
	Z is 0.

diet(X, Y, Z) :-
	X is 0,
	Y is 1,
	Z is 0.

diet(X, Y, Z) :-
	X is 0,
	Y is 0,
	Z is 1.


% min_in_list finds the minimum element of the list
% Current minimum element is kept on the head
% and is compared with the next element of the list
% if the element is smaller, minimum is set to that value
% the larger of both elements is deleted
% First argument contains the list, while 3rd argument contains the minimum value
% Fourth Argument contains the corresponding value in list provided as second argument
min_in_list([Min],[Val],Min,Val).

min_in_list([H,K|T],[A,_|B],M,N) :-
    H =< K,
    min_in_list([H|T],[A|B],M,N).

min_in_list([H,K|T],[_,A|B],M,N) :-
    H > K,
    min_in_list([K|T],[A|B],M,N).