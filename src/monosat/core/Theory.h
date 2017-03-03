/****************************************************************************************[Solver.h]
 The MIT License (MIT)

 Copyright (c) 2014, Sam Bayless

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 associated documentation files (the "Software"), to deal in the Software without restriction,
 including without limitation the rights to use, copy, modify, merge, publish, distribute,
 sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or
 substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **************************************************************************************************/

#ifndef THEORY_H_
#define THEORY_H_

#include "monosat/mtl/Vec.h"
#include "monosat/mtl/Heap.h"
#include "monosat/mtl/Alg.h"
#include "monosat/utils/Options.h"
#include "monosat/core/SolverTypes.h"
#include <ostream>
namespace Monosat {
/**
 * Abstract interface to SMT theory solvers, as accessed by the SAT solver
 */
class Theory {
	int priority=0;
	double activity=0;
public:
	virtual ~Theory() {
	}

    int getPriority()const{
		return priority;
	}
    void setPriority(int p){
    	priority=p;
    }

    double & getActivity(){
		return activity;
	}
    void setActivity(double p){
    	activity=p;
    }

	virtual int getTheoryIndex()=0;
	virtual void setTheoryIndex(int id)=0;
	virtual void backtrackUntil(int untilLevel)=0;
	virtual void newDecisionLevel()=0;
	virtual void enqueueTheory(Lit p)=0;
	virtual void enqueueAnyUnqueued(){

	}
	virtual void clearSatisfied(){

	}
	virtual bool propagateTheory(vec<Lit> & conflict)=0;
	virtual bool solveTheory(vec<Lit> & conflict)=0;
	virtual bool theoryIsSatisfied(){
		return false;
	}
	virtual Lit decideTheory(CRef & decision_reason) {
		decision_reason = CRef_Undef;
		return lit_Undef;
	}
	virtual bool supportsDecisions() {
		return false;
	}
	virtual void undecideTheory(Lit l){

	}

	//Lazily construct the reason clause explaining this propagation
	virtual void buildReason(Lit p, vec<Lit> & reason, CRef reason_marker){
		return buildReason(p,reason);
	}
	virtual bool supportsLazyBacktracking(){
		return false;
	}



protected:
	virtual void buildReason(Lit p, vec<Lit> & reason){

	}
public:
	//Informs the theory solver about whether this literal (with this polarity!) ever occurs in its parent solver
	virtual void setLiteralOccurs(Lit l, bool occurs) {
		
	}
	virtual void printStats(int detailLevel = 0) {
		
	}
	virtual bool check_propagated(){
		return true;
	}
	virtual bool check_solved() {
		return true;
	}
	virtual void printSolution() {
		
	}
	virtual void writeTheoryWitness(std::ostream& write_to) {
		//do nothing
	}
	virtual void preprocess(){

	}
};

}

#endif /* THEORY_H_ */
