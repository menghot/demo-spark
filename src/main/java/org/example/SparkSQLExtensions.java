package org.example;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSessionExtensions;
import org.apache.spark.sql.catalyst.FunctionIdentifier;
import org.apache.spark.sql.catalyst.TableIdentifier;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.catalyst.parser.ParseException;
import org.apache.spark.sql.catalyst.parser.ParserInterface;
import org.apache.spark.sql.catalyst.plans.logical.AppendData;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.catalyst.plans.logical.ReplaceData;
import org.apache.spark.sql.catalyst.plans.logical.ReplaceTableAsSelect;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Function1;
import scala.collection.Seq;
import scala.runtime.BoxedUnit;


public class SparkSQLExtensions implements Function1<SparkSessionExtensions, BoxedUnit> {

    private static final Logger log = LoggerFactory.getLogger(SparkSQLExtensions.class);

    // check logic plan rule
    private static Function1<LogicalPlan, BoxedUnit> checkRule(SparkSession session) {
        return logicalPlan -> {
            if (logicalPlan instanceof ReplaceTableAsSelect || logicalPlan instanceof AppendData || logicalPlan instanceof ReplaceData) {
                log.info("logic plan >>>>>>>>>>>> {}", logicalPlan.prettyJson());
            }
            return BoxedUnit.UNIT;
        };
    }

    @Override
    public BoxedUnit apply(SparkSessionExtensions extensions) {
        extensions.injectCheckRule(SparkSQLExtensions::checkRule);
        extensions.injectParser(SQLParserTracer::new);
        return BoxedUnit.UNIT;
    }

    // trace the sql
    private static class SQLParserTracer implements ParserInterface {

        private static final Logger logger = LoggerFactory.getLogger(SQLParserTracer.class);
        private final ParserInterface delegate;

        public SQLParserTracer(SparkSession session, ParserInterface delegate) {
            this.delegate = delegate;
        }

        public LogicalPlan parsePlan(String sqlText) throws ParseException {

            LogicalPlan logicalPlan = delegate.parsePlan(sqlText);

            // Log the SQL text, send to remote service for further processing
            if (sqlText.toLowerCase().contains("merge") || sqlText.toLowerCase().contains("insert")) {
                logger.info("SQL Text >>>>>>>  {}", sqlText);
                logger.info("LogicalPlan >>>>>>>  {}", logicalPlan.prettyJson());
            }

            return logicalPlan;
        }

        public Expression parseExpression(String sqlText) throws ParseException {
            return delegate.parseExpression(sqlText);
        }

        public TableIdentifier parseTableIdentifier(String sqlText) throws ParseException {
            return delegate.parseTableIdentifier(sqlText);
        }

        public FunctionIdentifier parseFunctionIdentifier(String sqlText) throws ParseException {
            return delegate.parseFunctionIdentifier(sqlText);
        }

        public Seq<String> parseMultipartIdentifier(String sqlText) throws ParseException {
            return delegate.parseMultipartIdentifier(sqlText);
        }

        public StructType parseTableSchema(String sqlText) throws ParseException {
            return delegate.parseTableSchema(sqlText);
        }

        public DataType parseDataType(String sqlText) throws ParseException {
            return delegate.parseDataType(sqlText);
        }

        public LogicalPlan parseQuery(String sqlText) throws ParseException {
            return delegate.parseQuery(sqlText);
        }
    }
}
