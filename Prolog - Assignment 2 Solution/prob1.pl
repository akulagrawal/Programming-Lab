%:- use_module(library(clpfd)).   	% Uncomment to use #=

% Zero indexing is used in the whole program unless specified

decode(Str) :-
	string_length(Str, Y),			% Get length of string Str in Y 
	find(Str, 0, Y-1, N),			% Get solution for whole string i.e.
									% substring from index 0 to Y-1 and store it in N
	writeln(N).

% Find solution(Number of possible messages)
% for substring of Str whose 
% first letter is at index Idx 
% and last letter is at index Len
% Answer is stored in N
find(Str, Idx, Len, N) :-
	Idx1 is Idx+1,
	isdigit(Str, Idx),
	((Idx < Len) -> find(Str, Idx1, Len, N1) ; N1 is 1),	% Decode letter at index Idx as a single digit number
															% and store result of decoding rest of the string in N1
	Idx2 is Idx+2,
	(valid(Str, Idx, Len) -> ((Idx2 =< Len) -> find(Str, Idx2, Len, N2) ;
							  N2 is 1) ;					% Decode letters at index Idx and Idx+1 as a double digit number
							 N2 is 0),						% and store result of decoding rest of the string in N2									
	N is N1+N2.

%Check if letters at index Idx and Idx+1
%form a valid 2 digit number representing an alphabet
valid(Str, Idx, Len) :- 
	Idx =< Len-1,
	sub_atom(Str, Idx, 1, _, S1),		% Get subatom of length 1 at index Idx and store in S1
	Idx1 is Idx+1,
	sub_atom(Str, Idx1, 1, _, S2),		% Get subatom of length 1 at index Idx+1 and store in S2
	isalpha(S1,S2).

% Check if number formed by two digits X and Y
% is between 0 and 26, inclusive
isalpha(X,Y) :- (X @< '2') ; (X = '2', Y @=< '6').

% Check if letter at index Idx in string Str is a valid decimal digit
isdigit(Str, Idx) :- sub_atom(Str, Idx, 1, _, X), X @>= '0', X @=< '9'.