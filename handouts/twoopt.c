#include <stdlib.h>

void
twoopt(n, w, route, tweight)
int n, *w, *route, *tweight;
{

	/*
	  This function is a local search heuristic which, when
	  given an initial TSP tour, H, it deletes 2 edges from
	  H and reconnects the nodes so it is still a tour.  If
	  the new tour, H', has a lower tour weight, then H'
	  replaces H.  This process is continued until no further
	  improvement is possible.

	  NOTE:  This function is currently set up for the symmetric
			TSP.

	  NOTE:  Although pathological cases have been constructed,
			in general, a better initial tour provides a
			better final solution.  It is thus recommended
			that an initial tour be the output from a
			function like fitsp().

	  Input:  n = number of nodes in given TSP.
		  w = n by n weight matrix of given network.  The
			diagonal entries are not used.
		  route = vector of length n specifying the initial
				TSP route.
		  tweight = total weight of initial route.

	  Output:  route = vector specifying the final route.
		   tweight = total weight of route.

	  Programmer:  R. J. Craig
		       kat3@ihgp.ih.att.com
		       (708) 979-1822
		       AT&T Bell Labs
		       1200 E. Warrenville Rd.
		       Room:  1A-365
		       Naperville, IL.  60566-7045
	*/

	int ahead, i, i1, i2, index, j, j1, j2, last, limit, max, max1,
		n1, n2, next, s1, s2, t1, t2, *ptr;

	/* initialization */

	n1 = n - 1;
	n2 = n1 - 1;
	ptr = (int *)malloc(n*sizeof(int));
	if (ptr == NULL) {
		printf("twoopt:  insufficient memory\n");
		exit(1);
	}

	for (i=0; i<n1; i++)
		ptr[route[i]] = route[i+1];
	ptr[route[n1]] = route[0];

	do {
		max = 0;
		i1 = 0;
		for (i=0; i<n2; i++) {
			limit = (i == 0) ? n1:n;
			i2 = ptr[i1];
			j1 = ptr[i2];
			for (j=i+2; j<limit; j++) {
				j2 = ptr[j1];
				max1 = w[i2+i1*n]+w[j2+j1*n] -
					(w[j1+i1*n]+w[j2+i2*n]);

				/*
				  A better pair of edges has been found,
				  thus save them.
				*/

				if (max1 > max) {
					s1 = i1;
					s2 = i2;
					t1 = j1;
					t2 = j2;
					max = max1;
				}
				j1 = j2;
			}
			i1 = i2;
		}

		/*
		  We have found a better route, so update
		  the information.
		*/

		if (max > 0) {

			/*
			  Swap pair of edges.
			*/

			ptr[s1] = t1;
			next = s2;
			last = t2;

			/*
			  Reverse appropriate links.
			*/

			do {
				ahead = ptr[next];
				ptr[next] = last;
				last = next;
				next = ahead;
			} while (next != t2);

			/*
			  Reduce old route weight by required amount
			  to reflect the new route.
			*/

			*tweight -= max;
		}
	} while (max != 0);

	/*
	  Update route to reflect better route found.
	*/

	index = 0;
	for (i=0; i<n; i++) {
		route[i] = index;
		index = ptr[index];
	}
	free((char *) ptr);
}
