import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluatorResolver;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFParameterInfo;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFResolver2;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;

import java.util.List;

public class Maxmum2 implements GenericUDAFResolver2{

    @Override
    public GenericUDAFEvaluator getEvaluator(GenericUDAFParameterInfo genericUDAFParameterInfo) throws SemanticException {
        return null;
    }

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] typeInfos) throws SemanticException {
        return null;
    }
}
