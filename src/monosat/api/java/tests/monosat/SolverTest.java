/*
 The MIT License (MIT)

 Copyright (c) 2018, Sam Bayless

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
 */

package monosat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SolverTest {
    @Test
    public void testSolve() {
        Solver s = new Solver();
        assertTrue(s.solve());
        assertTrue(s.solve());
        assertTrue(s.solve(Lit.True));
        assertFalse(s.solve(Lit.False));
        assertFalse(s.solve(Lit.True, Lit.False));
        assertTrue(s.solve());
        System.out.println("Done");
    }
    @Test
    public void testArguments() {
        assertTrue(false); //testing travis errors
        Solver s = new Solver("-no-reach-underapprox-cnf");
        assertTrue(s.solve());
        assertTrue(s.solve());
        assertTrue(s.solve(Lit.True));
        assertFalse(s.solve(Lit.False));
        assertFalse(s.solve(Lit.True, Lit.False));
        assertTrue(s.solve());
        System.out.println("Done");
    }
    @Test
    public void testMultipleSolvers() {
        Solver s = new Solver();
        assertTrue(s.solve());

        Solver s2 = new Solver();
        assertTrue(s2.solve());
        assertTrue(s.solve());
        s.close();

        assertTrue(s2.solve());
    }

    @Test
    public void testOrUnsat(){
        Solver s = new Solver();
        assertTrue(s.solve());
        s.assertAnd();
        assertTrue(s.solve());
        s.assertOr();
        assertFalse(s.solve());
        Solver s2 = new Solver();
        assertTrue(s2.solve());
        assertFalse(s.solve());
    }


    @Test
    public void testAddClause(){
        Solver s = new Solver();
        assertTrue(s.solve());
        s.addClause();
        assertFalse(s.solve());
        s = new Solver();
        Lit a = new Lit(s);
        Lit b = new Lit(s);
        Lit c = new Lit(s);
        Lit d = new Lit(s);
        s.addClause(a);
        s.addClause(a,b.not());
        s.addClause(a.not(),b,c);
        s.addClause(a.not(),b,c,d.not());
        assertTrue(s.solve());
        ArrayList<Lit> clause = new ArrayList<Lit>(Arrays.asList(a,b.not(),c.not(),d));
        s.addClause(clause);
        assertTrue(s.solve());
    }

    @Test
    public void test_AMO() {
        Solver solver = new Solver();
        Lit a = new Lit(solver);
        Lit b = new Lit(solver);
        Lit c = new Lit(solver);
        Lit result = Logic.xnor(a, b, c);
        assertTrue(solver.solve());
        //An empty AMO call should have no effect
        solver.assertAtMostOne();
        assertTrue(solver.solve());
        solver.assertAtMostOne(a);
        assertTrue(solver.solve());
        assertTrue(solver.solve(a));
        assertTrue(solver.solve(a.not()));

        solver.assertAtMostOne(a,b);
        assertTrue(solver.solve());
        assertTrue(solver.solve(a));
        assertTrue(solver.solve(a.not(),b.not()));
    }

    @Test
    public void getLits() {
        Solver s = new Solver();
        assertEquals(s.getLits().size(),1);
        Lit a = new Lit(s);
        Lit b = new Lit(s);
        Lit c = new Lit(s);
        Lit d = new Lit(s);
        assertEquals(s.getLits().size(),5);
        s.addClause(a);
        s.addClause(a,b.not());
        s.addClause(a.not(),b,c);
        s.addClause(a.not(),b,c,d.not());
        assertTrue(s.solve());
        ArrayList<Lit> clause = new ArrayList<Lit>(Arrays.asList(a,b.not(),c.not(),d));
        s.addClause(clause);
        assertTrue(s.solve());

        assertEquals(s.getLits().size(),5); //plus 1, for the constant true literal
        for(Lit l:s.getLits()){
            boolean val = l.value();
        }
    }

    @Test
    public void version(){
        String version  = Solver.getVersion();
        System.out.println(version);
        assertFalse(version.isEmpty());
    }

}
